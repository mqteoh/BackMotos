package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ReportesDTO.CostosSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.DisponiblesSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.RepuestosSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.Resumen;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.VentasMes;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.VentasVendedor;
import co.edu.unbosque.BackMotos.exception.BadRequestException;

/** Reportes del dueño, calculados con SQL sobre las tablas y vistas. */
@Service
@Transactional(readOnly = true)
public class ReporteService {

	/** Ejecuta las consultas SQL de los reportes. */
	private final NamedParameterJdbcTemplate jdbc;
	/** Reloj para saber la fecha y hora actual. */
	private final Clock reloj;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param jdbc ejecuta las consultas SQL de los reportes
	 * @param reloj reloj para saber la fecha y hora actual
	 */
	public ReporteService(NamedParameterJdbcTemplate jdbc, Clock reloj) {
		this.jdbc = jdbc;
		this.reloj = reloj;
	}

	/**
	 * Cifras del mes actual.
	 *
	 * @return cifras del mes actual
	 */
	public Resumen resumen() {
		YearMonth mes = YearMonth.now(reloj);
		MapSqlParameterSource p = new MapSqlParameterSource().addValue("desde", mes.atDay(1).atStartOfDay())
				.addValue("hasta", mes.plusMonths(1).atDay(1).atStartOfDay());
		return jdbc.queryForObject("""
				SELECT (SELECT COUNT(*) FROM v_moto_disponible) AS disponibles,
				       (SELECT COUNT(*) FROM reserva WHERE estado = 'ACTIVA') AS reservas,
				       (SELECT COUNT(*) FROM reparacion WHERE fecha >= CAST(:desde AS DATE) AND fecha < CAST(:hasta AS DATE)) AS reparaciones_mes,
				       (SELECT COUNT(*) FROM venta_moto WHERE fecha_venta >= :desde AND fecha_venta < :hasta) AS motos_mes,
				       (SELECT COALESCE(SUM(valor_total), 0) FROM venta_moto WHERE fecha_venta >= :desde AND fecha_venta < :hasta) AS total_motos,
				       (SELECT COALESCE(SUM(valor_total), 0) FROM venta_accesorio WHERE fecha_venta >= :desde AND fecha_venta < :hasta) AS total_accesorios,
				       (SELECT COUNT(*) FROM cliente) AS clientes,
				       (SELECT COUNT(*) FROM empleado) AS empleados
				""", p,
				(rs, i) -> new Resumen(mes.toString(), rs.getLong("disponibles"), rs.getLong("reservas"),
						rs.getLong("reparaciones_mes"), rs.getLong("motos_mes"), rs.getBigDecimal("total_motos"),
						rs.getBigDecimal("total_accesorios"), rs.getLong("clientes"), rs.getLong("empleados")));
	}

	/**
	 * Ventas de cada vendedor, de mayor a menor.
	 *
	 * @return ventas de cada vendedor, de mayor a menor
	 */
	public List<VentasVendedor> ventasPorVendedor() {
		return jdbc.query("""
				SELECT id_empleado, vendedor, sucursal, motos_vendidas, total_motos, total_accesorios
				FROM v_ventas_por_vendedor ORDER BY total_motos + total_accesorios DESC
				""", (rs, i) -> {
			BigDecimal motos = rs.getBigDecimal("total_motos");
			BigDecimal accesorios = rs.getBigDecimal("total_accesorios");
			return new VentasVendedor(rs.getInt("id_empleado"), rs.getString("vendedor"), rs.getString("sucursal"),
					rs.getLong("motos_vendidas"), motos, accesorios, motos.add(accesorios));
		});
	}

	/**
	 * Ventas de cada mes de un año; los meses sin ventas salen en cero.
	 *
	 * @param anio año; null para el año actual
	 * @return ventas de cada mes de un año; los meses sin ventas salen en cero
	 */
	public List<VentasMes> ventasPorMes(Integer anio) {
		int consulta = anio == null ? LocalDate.now(reloj).getYear() : anio;
		if (consulta < 2000 || consulta > 2100) {
			throw new BadRequestException("El año debe estar entre 2000 y 2100.");
		}
		return jdbc.query("""
				WITH RECURSIVE meses (mes) AS (SELECT 1 UNION ALL SELECT mes + 1 FROM meses WHERE mes < 12)
				SELECT m.mes,
				       (SELECT COUNT(*) FROM venta_moto v WHERE EXTRACT(YEAR FROM v.fecha_venta) = :anio AND EXTRACT(MONTH FROM v.fecha_venta) = m.mes) AS motos,
				       (SELECT COALESCE(SUM(v.valor_total), 0) FROM venta_moto v WHERE EXTRACT(YEAR FROM v.fecha_venta) = :anio AND EXTRACT(MONTH FROM v.fecha_venta) = m.mes) AS total_motos,
				       (SELECT COUNT(*) FROM venta_accesorio t WHERE EXTRACT(YEAR FROM t.fecha_venta) = :anio AND EXTRACT(MONTH FROM t.fecha_venta) = m.mes) AS ventas_acc,
				       (SELECT COALESCE(SUM(t.valor_total), 0) FROM venta_accesorio t WHERE EXTRACT(YEAR FROM t.fecha_venta) = :anio AND EXTRACT(MONTH FROM t.fecha_venta) = m.mes) AS total_acc
				FROM meses m ORDER BY m.mes
				""", new MapSqlParameterSource("anio", consulta), (rs, i) -> {
			BigDecimal motos = rs.getBigDecimal("total_motos");
			BigDecimal accesorios = rs.getBigDecimal("total_acc");
			return new VentasMes(rs.getInt("mes"), rs.getLong("motos"), motos, rs.getLong("ventas_acc"), accesorios,
					motos.add(accesorios));
		});
	}

	/**
	 * Costos de reparación por sede, desde la vista v_sede_reparacion.
	 *
	 * @return costos de reparación por sede, desde la vista v_sede_reparacion
	 */
	public List<CostosSede> costosReparacion() {
		return jdbc.query("""
				SELECT s.id_sucursal, s.nombre,
				       COUNT(c.id_reparacion) AS reparaciones,
				       COALESCE(SUM(c.mano_obra), 0) AS mano_obra,
				       COALESCE(SUM(c.repuestos), 0) AS repuestos,
				       COALESCE(SUM(c.costo_total), 0) AS total
				FROM sucursal s
				LEFT JOIN v_sede_reparacion sr ON sr.id_sucursal = s.id_sucursal
				LEFT JOIN v_costo_reparacion c ON c.id_reparacion = sr.id_reparacion
				GROUP BY s.id_sucursal, s.nombre ORDER BY s.nombre
				""", (rs, i) -> new CostosSede(rs.getInt("id_sucursal"), rs.getString("nombre"), rs.getLong("reparaciones"),
				rs.getBigDecimal("mano_obra"), rs.getBigDecimal("repuestos"), rs.getBigDecimal("total")));
	}

	/**
	 * Repuestos recibidos por sede, desde la vista v_repuestos_por_sucursal.
	 *
	 * @param idSucursal identificador de la sede
	 * @return repuestos recibidos por sede, desde la vista v_repuestos_por_sucursal
	 */
	public List<RepuestosSede> repuestosPorSucursal(Integer idSucursal) {
		return jdbc.query("""
				SELECT id_sucursal, sucursal, id_repuestos, repuesto, unidades_recibidas, unidades_usadas
				FROM v_repuestos_por_sucursal
				WHERE (unidades_recibidas > 0 OR unidades_usadas > 0)
				  AND (CAST(:idSucursal AS INTEGER) IS NULL OR id_sucursal = CAST(:idSucursal AS INTEGER))
				ORDER BY sucursal, repuesto
				""", new MapSqlParameterSource("idSucursal", idSucursal),
				(rs, i) -> new RepuestosSede(rs.getInt("id_sucursal"), rs.getString("sucursal"), rs.getInt("id_repuestos"),
						rs.getString("repuesto"), rs.getLong("unidades_recibidas"), rs.getLong("unidades_usadas")));
	}

	/**
	 * Motos disponibles en cada sede.
	 *
	 * @return motos disponibles en cada sede
	 */
	public List<DisponiblesSede> disponiblesPorSede() {
		return jdbc.query("""
				SELECT s.id_sucursal, s.nombre, s.ciudad, COUNT(d.placa) AS disponibles
				FROM sucursal s LEFT JOIN v_moto_disponible d ON d.id_sucursal = s.id_sucursal
				GROUP BY s.id_sucursal, s.nombre, s.ciudad ORDER BY s.nombre
				""", (rs, i) -> new DisponiblesSede(rs.getInt("id_sucursal"), rs.getString("nombre"),
				rs.getString("ciudad"), rs.getLong("disponibles")));
	}
}
