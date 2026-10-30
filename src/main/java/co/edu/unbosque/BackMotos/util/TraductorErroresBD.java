package co.edu.unbosque.BackMotos.util;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

public final class TraductorErroresBD {

	public record ErrorBD(HttpStatus estado, String mensaje) {
	}

	private static final String SQLSTATE_REGLA_NEGOCIO = "45000";
	private static final int DUPLICADO = 1062;
	private static final int NULO_NO_PERMITIDO = 1048;
	private static final int REGISTRO_CON_DEPENDIENTES = 1451;
	private static final int REFERENCIA_INEXISTENTE = 1452;
	private static final int CHECK_VIOLADO = 3819;

	private static final Pattern NOMBRE_CHECK = Pattern.compile("Check constraint '(\\w+)'");
	private static final Pattern NOMBRE_CLAVE = Pattern.compile("for key '(?:\\w+\\.)?(\\w+)'");

	private static final String CANTIDAD = "La cantidad debe ser mayor que cero.";
	private static final String PRECIO = "El precio debe ser mayor que cero.";
	private static final String PRECIO_UNITARIO = "El precio unitario debe ser mayor que cero.";
	private static final String TELEFONO = "El teléfono debe tener entre 7 y 15 dígitos.";
	private static final String CORREO = "El correo no es válido.";
	private static final String PAGO = "El método de pago no es válido.";
	private static final String FACTURA = "El tipo de factura no es válido.";

	private static final Map<String, String> REGLAS = Map.ofEntries(
			Map.entry("ck_modelo_nro", "El número de modelo debe ser mayor que cero."),
			Map.entry("ck_modelo_cilindraje", "El cilindraje debe estar entre 50 y 2500 cc."),
			Map.entry("ck_modelo_tipo", "El tipo de moto no es válido."),
			Map.entry("ck_modelo_potencia", "La potencia debe ser mayor que cero."),
			Map.entry("ck_modelo_tanque", "La capacidad del tanque debe ser mayor que cero."),
			Map.entry("ck_moto_placa", "La placa debe tener el formato AAA00A."),
			Map.entry("ck_moto_origen", "El origen de la moto debe ser INVENTARIO o EXTERNA."),
			Map.entry("ck_moto_anio", "El año de la moto debe estar entre 1980 y 2030."),
			Map.entry("ck_moto_precio", PRECIO),
			Map.entry("ck_moto_origen_datos",
					"Una moto de inventario necesita sede y precio; una moto externa no lleva ninguno de los dos."),
			Map.entry("ck_cliente_cuenta", "La cuenta web necesita correo de acceso y contraseña."),
			Map.entry("ck_cliente_correo_acceso", CORREO),
			Map.entry("ck_telefono_cliente_numero", TELEFONO),
			Map.entry("ck_correo_cliente_formato", CORREO),
			Map.entry("ck_empleado_tipo", "El tipo de empleado debe ser DUENO, VENDEDOR o TECNICO."),
			Map.entry("ck_empleado_sucursal", "Todo empleado, salvo el dueño, debe tener sede."),
			Map.entry("ck_empleado_correo_acceso", CORREO),
			Map.entry("ck_telefono_empleado_numero", TELEFONO),
			Map.entry("ck_vendedor_comision", "La comisión debe estar entre 0 y 100."),
			Map.entry("ck_accesorio_precio", PRECIO),
			Map.entry("ck_reserva_estado", "El estado de la reserva no es válido."),
			Map.entry("ck_reserva_vencimiento", "La fecha de vencimiento no puede ser anterior a la fecha de la reserva."),
			Map.entry("ck_venta_moto_pago", PAGO),
			Map.entry("ck_venta_moto_factura", FACTURA),
			Map.entry("ck_venta_moto_valor", "El valor de la venta debe ser mayor que cero."),
			Map.entry("ck_venta_accesorio_pago", PAGO),
			Map.entry("ck_venta_accesorio_factura", FACTURA),
			Map.entry("ck_detalle_accesorio_cantidad", CANTIDAD),
			Map.entry("ck_detalle_accesorio_precio", PRECIO_UNITARIO),
			Map.entry("ck_repuesto_precio", PRECIO),
			Map.entry("ck_reparacion_estado", "El estado de la reparación no es válido."),
			Map.entry("ck_reparacion_costo", "El costo no puede ser negativo."),
			Map.entry("ck_reparacion_tecnico", "Una reparación en proceso o finalizada necesita un técnico asignado."),
			Map.entry("ck_reparacion_cierre",
					"Para finalizar una reparación se necesitan el costo y la fecha de finalización."),
			Map.entry("ck_reparacion_fechas", "La fecha de finalización no puede ser anterior a la fecha agendada."),
			Map.entry("ck_repuesto_reparacion_cantidad", CANTIDAD),
			Map.entry("ck_repuesto_reparacion_precio", PRECIO_UNITARIO),
			Map.entry("ck_proveedor_nit", "El NIT debe tener el formato 000000000-0."),
			Map.entry("ck_suministro_cantidad", CANTIDAD),
			Map.entry("ck_suministro_costo", "El costo unitario debe ser mayor que cero."),
			Map.entry("uq_marca_nombre", "Ya existe una marca con ese nombre."),
			Map.entry("uq_modelo_id_marca_nombre", "La marca ya tiene un modelo con ese nombre."),
			Map.entry("uq_sucursal_nombre", "Ya existe una sede con ese nombre."),
			Map.entry("uq_cliente_documento", "Ya existe un cliente con ese documento."),
			Map.entry("uq_cliente_correo_acceso", "Ese correo de acceso ya está registrado."),
			Map.entry("uq_empleado_documento", "Ya existe un empleado con ese documento."),
			Map.entry("uq_empleado_correo_acceso", "Ese correo de acceso ya está registrado."),
			Map.entry("uq_venta_moto_placa", "Esta moto ya fue vendida."),
			Map.entry("uq_venta_moto_id_reserva", "Esta reserva ya se usó en otra venta."),
			Map.entry("uq_proveedor_nombre", "Ya existe un proveedor con ese nombre."));

	private TraductorErroresBD() {
	}

	public static Optional<ErrorBD> traducir(Throwable error) {
		SQLException sql = buscarSQLException(error);
		if (sql == null) {
			return Optional.empty();
		}
		String mensaje = sql.getMessage() == null ? "" : sql.getMessage();
		if (SQLSTATE_REGLA_NEGOCIO.equals(sql.getSQLState())) {
			return Optional.of(new ErrorBD(HttpStatus.CONFLICT, mensaje));
		}
		return switch (sql.getErrorCode()) {
		case CHECK_VIOLADO -> Optional.of(new ErrorBD(HttpStatus.CONFLICT,
				porNombre(NOMBRE_CHECK, mensaje, "La operación no cumple una regla de la base de datos.")));
		case DUPLICADO -> Optional.of(new ErrorBD(HttpStatus.CONFLICT,
				porNombre(NOMBRE_CLAVE, mensaje, "Ya existe un registro con esa clave.")));
		case REGISTRO_CON_DEPENDIENTES -> Optional.of(new ErrorBD(HttpStatus.CONFLICT,
				"No se puede eliminar porque otros registros dependen de él."));
		case REFERENCIA_INEXISTENTE -> Optional.of(new ErrorBD(HttpStatus.BAD_REQUEST,
				"Uno de los registros relacionados no existe."));
		case NULO_NO_PERMITIDO -> Optional.of(new ErrorBD(HttpStatus.BAD_REQUEST, "Falta un dato obligatorio."));
		default -> Optional.empty();
		};
	}

	private static String porNombre(Pattern patron, String mensaje, String porDefecto) {
		Matcher m = patron.matcher(mensaje);
		return m.find() ? REGLAS.getOrDefault(m.group(1), porDefecto) : porDefecto;
	}

	private static SQLException buscarSQLException(Throwable error) {
		SQLException encontrada = null;
		for (Throwable t = error; t != null && t.getCause() != t; t = t.getCause()) {
			if (t instanceof SQLException sql) {
				encontrada = sql;
			}
		}
		return encontrada;
	}
}
