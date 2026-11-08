SET search_path TO concesionario_motos;

CREATE OR REPLACE VIEW v_moto_disponible AS
SELECT m.placa, ma.nombre AS marca, mo.nombre AS modelo, mo.tipo_moto, mo.cilindraje,
       m.anio, m.color, m.precio, s.id_sucursal, s.nombre AS sucursal, s.ciudad
FROM moto m
JOIN modelo mo ON mo.id_marca = m.id_marca AND mo.id_modelo = m.id_modelo
JOIN marca ma ON ma.id_marca = m.id_marca
JOIN sucursal s ON s.id_sucursal = m.id_sucursal
WHERE m.estado = 'DISPONIBLE';

CREATE OR REPLACE VIEW v_sede_reparacion AS
SELECT DISTINCT tr.id_reparacion, e.id_sucursal
FROM tecnicos_reparacion tr
JOIN empleado e ON e.id_empleado = tr.id_empleado;

CREATE OR REPLACE VIEW v_costo_reparacion AS
SELECT r.id_reparacion, r.placa, r.fecha, r.costo AS mano_obra,
       COALESCE(SUM(rr.cantidad * rr.precio_unitario), 0) AS repuestos,
       r.costo + COALESCE(SUM(rr.cantidad * rr.precio_unitario), 0) AS costo_total
FROM reparacion r
LEFT JOIN reparacion_repuestos rr ON rr.id_reparacion = r.id_reparacion
GROUP BY r.id_reparacion, r.placa, r.fecha, r.costo;

CREATE OR REPLACE VIEW v_ventas_por_vendedor AS
SELECT e.id_empleado, CONCAT(e.nombres, ' ', e.apellidos) AS vendedor, s.nombre AS sucursal,
       (SELECT COUNT(*) FROM venta_moto vm WHERE vm.id_vendedor = e.id_empleado) AS motos_vendidas,
       (SELECT COALESCE(SUM(vm.valor_total), 0) FROM venta_moto vm WHERE vm.id_vendedor = e.id_empleado) AS total_motos,
       (SELECT COALESCE(SUM(va.valor_total), 0) FROM venta_accesorio va WHERE va.id_vendedor = e.id_empleado) AS total_accesorios
FROM vendedor v
JOIN empleado e ON e.id_empleado = v.id_empleado
JOIN sucursal s ON s.id_sucursal = e.id_sucursal;

CREATE OR REPLACE VIEW v_repuestos_por_sucursal AS
SELECT s.id_sucursal, s.nombre AS sucursal, rp.id_repuestos, rp.nombre AS repuesto,
       COALESCE((SELECT SUM(su.cantidad) FROM suministros su
                 WHERE su.id_sucursal = s.id_sucursal AND su.id_repuestos = rp.id_repuestos), 0) AS unidades_recibidas,
       COALESCE((SELECT SUM(rr.cantidad) FROM reparacion_repuestos rr
                 JOIN v_sede_reparacion sr ON sr.id_reparacion = rr.id_reparacion
                 WHERE sr.id_sucursal = s.id_sucursal AND rr.id_repuestos = rp.id_repuestos), 0) AS unidades_usadas
FROM sucursal s
CROSS JOIN repuesto rp;

CREATE OR REPLACE FUNCTION sp_vencer_reservas() RETURNS INTEGER LANGUAGE plpgsql AS $$
DECLARE
    vencidas INTEGER;
BEGIN
    UPDATE reserva SET estado = 'VENCIDA'
    WHERE estado = 'ACTIVA' AND fecha_vencimiento < CAST(now() AT TIME ZONE 'America/Bogota' AS DATE);
    GET DIAGNOSTICS vencidas = ROW_COUNT;
    RETURN vencidas;
END $$;

CREATE OR REPLACE FUNCTION sp_historial_moto(p_placa VARCHAR)
RETURNS TABLE (id_reparacion INTEGER, fecha DATE, descripcion VARCHAR, tecnicos TEXT, costo_total NUMERIC)
LANGUAGE sql AS $$
    SELECT r.id_reparacion, r.fecha, r.descripcion,
           (SELECT STRING_AGG(e.nombres || ' ' || e.apellidos, ', ' ORDER BY e.apellidos)
            FROM tecnicos_reparacion tr JOIN empleado e ON e.id_empleado = tr.id_empleado
            WHERE tr.id_reparacion = r.id_reparacion),
           c.costo_total
    FROM reparacion r
    JOIN v_costo_reparacion c ON c.id_reparacion = r.id_reparacion
    WHERE r.placa = p_placa
    ORDER BY r.fecha;
$$;
