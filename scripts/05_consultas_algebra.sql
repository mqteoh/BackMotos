-- Consultas SQL equivalentes a los 50 ejercicios de álgebra relacional, en el mismo orden.
SET search_path TO concesionario_motos;

-- 1. Obtener la placa y el precio de las motos disponibles con precio de lista mayor a 15 millones.
SELECT placa, precio FROM moto WHERE estado = 'DISPONIBLE' AND precio > 15000000;

-- 2. Obtener el nombre de las marcas de origen japonés.
SELECT nombre FROM marca WHERE pais_origen = 'Japón';

-- 3. Obtener el documento y el nombre de los clientes nacidos antes de 1990.
SELECT documento, nombres, apellidos FROM cliente WHERE fecha_nacimiento < '1990-01-01';

-- 4. Obtener las ventas de accesorios con factura electrónica que se pagaron por transferencia.
SELECT id_venta, fecha_venta, valor_total FROM venta_accesorio WHERE tipo_factura = 'ELECTRONICA' AND metodo_pago = 'TRANSFERENCIA';

-- 5. Obtener las reparaciones cuya mano de obra fue de al menos 100.000 pesos.
SELECT id_reparacion, placa, costo FROM reparacion WHERE costo >= 100000;

-- 6. Obtener los modelos de tipo scooter o doble propósito con su cilindraje.
SELECT nombre, cilindraje, tipo_moto FROM modelo WHERE tipo_moto = 'SCOOTER' OR tipo_moto = 'DOBLE_PROPOSITO';

-- 7. Obtener el nombre y el precio de los repuestos de la categoría FRENOS.
SELECT nombre, precio FROM repuesto WHERE categoria = 'FRENOS';

-- 8. Obtener las ventas de motos pagadas con tarjeta de crédito.
SELECT placa, fecha_venta, valor_total FROM venta_moto WHERE metodo_pago = 'TARJETA_CREDITO';

-- 9. Obtener los empleados con salario mayor a 3 millones.
SELECT documento, nombres, apellidos, salario FROM empleado WHERE salario > 3000000;

-- 10. Obtener las motos del año 2026 con menos de 100 kilómetros.
SELECT placa, color, kilometraje FROM moto WHERE anio = 2026 AND kilometraje < 100;

-- 11. Obtener cada modelo con el nombre de su marca y su cilindraje.
SELECT ma.nombre AS marca, mo.nombre AS modelo, mo.cilindraje FROM marca ma JOIN modelo mo ON ma.id_marca = mo.id_marca;

-- 12. Obtener la placa de las motos que están en sedes de Bogotá, con el nombre de la sede.
SELECT m.placa, s.nombre FROM sucursal s JOIN moto m ON s.id_sucursal = m.id_sucursal WHERE s.ciudad = 'Bogotá';

-- 13. Obtener las ventas de motos con el nombre del cliente y el del vendedor.
SELECT v.placa, c.nombres, e.nombres AS nombre_vendedor, v.valor_total FROM venta_moto v JOIN cliente c ON v.id_cliente = c.id_cliente JOIN empleado e ON v.id_vendedor = e.id_empleado;

-- 14. Obtener la placa, la marca y el modelo de cada moto vendida.
SELECT v.placa, ma.nombre AS marca, mo.nombre AS modelo FROM venta_moto v JOIN moto m ON v.placa = m.placa JOIN modelo mo ON m.id_marca = mo.id_marca AND m.id_modelo = mo.id_modelo JOIN marca ma ON mo.id_marca = ma.id_marca;

-- 15. Obtener las reparaciones con los técnicos que participaron en cada una.
SELECT r.id_reparacion, r.placa, e.nombres, e.apellidos FROM reparacion r JOIN tecnicos_reparacion tr ON r.id_reparacion = tr.id_reparacion JOIN empleado e ON tr.id_empleado = e.id_empleado;

-- 16. Obtener los repuestos usados en cada reparación con su cantidad.
SELECT rr.id_reparacion, rp.nombre, rr.cantidad FROM reparacion_repuestos rr JOIN repuesto rp ON rr.id_repuestos = rp.id_repuestos;

-- 17. Obtener los accesorios que ha comprado el cliente con documento 80765432.
SELECT DISTINCT a.nombre FROM cliente c JOIN venta_accesorio va ON c.id_cliente = va.id_cliente JOIN detalle_accesorio d ON va.id_venta = d.id_venta JOIN accesorio a ON d.id_accesorio = a.id_accesorio WHERE c.documento = '80765432';

-- 18. Obtener cada empleado con el nombre de su jefe.
SELECT e.nombres, e.apellidos, j.nombres AS nombre_jefe, j.apellidos AS apellido_jefe FROM empleado e JOIN empleado j ON e.id_jefe = j.id_empleado;

-- 19. Obtener los proveedores que han surtido repuestos a la Sede Laureles.
SELECT DISTINCT p.nombre FROM proveedor p JOIN suministros su ON p.nit = su.nit JOIN sucursal s ON su.id_sucursal = s.id_sucursal WHERE s.nombre = 'Sede Laureles';

-- 20. Obtener cada reparación con el nombre del cliente dueño de la moto.
SELECT r.id_reparacion, r.placa, r.fecha, c.nombres, c.apellidos FROM reparacion r JOIN venta_moto v ON r.placa = v.placa JOIN cliente c ON v.id_cliente = c.id_cliente ORDER BY r.id_reparacion;

-- 21. Obtener los técnicos con su sede y su especialidad.
SELECT e.nombres, e.apellidos, s.nombre, t.especialidad FROM tecnico t JOIN empleado e ON t.id_empleado = e.id_empleado JOIN sucursal s ON e.id_sucursal = s.id_sucursal;

-- 22. Listar todas las motos con su estado y, si se vendió, el valor de la venta.
SELECT m.placa, m.estado, v.valor_total FROM moto m LEFT JOIN venta_moto v ON m.placa = v.placa ORDER BY m.placa;

-- 23. Listar todos los clientes y, si compraron una moto en 2026, la placa y la fecha de la compra.
SELECT c.nombres, c.apellidos, v.placa, v.fecha_venta FROM cliente c LEFT JOIN venta_moto v ON c.id_cliente = v.id_cliente AND v.fecha_venta >= '2026-01-01' ORDER BY c.id_cliente;

-- 24. Obtener los documentos de todas las personas registradas, clientes y empleados.
SELECT documento FROM cliente UNION SELECT documento FROM empleado;

-- 25. Obtener los clientes que han comprado una moto o algún accesorio.
SELECT id_cliente FROM venta_moto UNION SELECT id_cliente FROM venta_accesorio;

-- 26. Obtener los clientes que han comprado una moto y también accesorios.
SELECT id_cliente FROM venta_moto INTERSECT SELECT id_cliente FROM venta_accesorio;

-- 27. Obtener los clientes que han comprado accesorios pero nunca una moto.
SELECT id_cliente FROM venta_accesorio EXCEPT SELECT id_cliente FROM venta_moto;

-- 28. Obtener los empleados que no son vendedores ni técnicos.
SELECT e.id_empleado, e.nombres, e.apellidos FROM empleado e WHERE e.id_empleado IN (SELECT id_empleado FROM empleado EXCEPT SELECT id_empleado FROM vendedor EXCEPT SELECT id_empleado FROM tecnico);

-- 29. Obtener los vendedores que no han vendido accesorios.
SELECT id_empleado FROM vendedor EXCEPT SELECT id_vendedor FROM venta_accesorio;

-- 30. Obtener los repuestos que llegaron por suministro y además se usaron en el taller.
SELECT id_repuestos FROM suministros INTERSECT SELECT id_repuestos FROM reparacion_repuestos;

-- 31. Obtener las motos vendidas que nunca han entrado al taller.
SELECT placa FROM venta_moto EXCEPT SELECT placa FROM reparacion;

-- 32. Obtener todas las combinaciones de sede y marca, para planear la exhibición.
SELECT s.nombre AS sede, ma.nombre AS marca FROM sucursal s CROSS JOIN marca ma;

-- 33. Obtener cada moto con el nombre de su sede usando producto cartesiano y selección.
SELECT m.placa, s.nombre FROM moto m CROSS JOIN sucursal s WHERE m.id_sucursal = s.id_sucursal;

-- 34. Obtener las parejas de motos distintas que son del mismo modelo.
SELECT m1.placa AS placa1, m2.placa AS placa2 FROM moto m1 JOIN moto m2 ON m1.id_marca = m2.id_marca AND m1.id_modelo = m2.id_modelo AND m1.placa < m2.placa;

-- 35. Obtener las parejas de técnicos que trabajaron juntos en una reparación.
SELECT t1.id_reparacion AS rep1, t1.id_empleado AS tec1, t2.id_empleado AS tec2 FROM tecnicos_reparacion t1 JOIN tecnicos_reparacion t2 ON t1.id_reparacion = t2.id_reparacion AND t1.id_empleado < t2.id_empleado;

-- 36. Obtener los datos de los accesorios que se han vendido al menos una vez.
SELECT a.id_accesorio, a.nombre, a.marca, a.precio FROM accesorio a WHERE EXISTS (SELECT 1 FROM detalle_accesorio d WHERE d.id_accesorio = a.id_accesorio) ORDER BY a.id_accesorio;

-- 37. Obtener los repuestos que nunca se han usado en una reparación.
SELECT rp.id_repuestos, rp.nombre, rp.categoria, rp.precio FROM repuesto rp WHERE NOT EXISTS (SELECT 1 FROM reparacion_repuestos rr WHERE rr.id_repuestos = rp.id_repuestos) ORDER BY rp.id_repuestos;

-- 38. Obtener los clientes que no han hecho ninguna compra de accesorios.
SELECT c.documento, c.nombres, c.apellidos FROM cliente c WHERE NOT EXISTS (SELECT 1 FROM venta_accesorio va WHERE va.id_cliente = c.id_cliente) ORDER BY c.id_cliente;

-- 39. Obtener los proveedores que han surtido a todas las sedes.
SELECT p.nit, p.nombre FROM proveedor p WHERE NOT EXISTS (SELECT 1 FROM sucursal s WHERE NOT EXISTS (SELECT 1 FROM suministros su WHERE su.nit = p.nit AND su.id_sucursal = s.id_sucursal));

-- 40. Obtener los clientes que han comprado todos los accesorios de la marca Shaft.
SELECT c.id_cliente, c.nombres, c.apellidos FROM cliente c WHERE NOT EXISTS (SELECT 1 FROM accesorio a WHERE a.marca = 'Shaft' AND NOT EXISTS (SELECT 1 FROM venta_accesorio va JOIN detalle_accesorio d ON va.id_venta = d.id_venta WHERE va.id_cliente = c.id_cliente AND d.id_accesorio = a.id_accesorio));

-- 41. Obtener los técnicos que han usado todos los repuestos de la categoría FRENOS.
SELECT t.id_empleado, e.nombres, e.apellidos FROM tecnico t JOIN empleado e ON t.id_empleado = e.id_empleado WHERE NOT EXISTS (SELECT 1 FROM repuesto rp WHERE rp.categoria = 'FRENOS' AND NOT EXISTS (SELECT 1 FROM tecnicos_reparacion tr JOIN reparacion_repuestos rr ON tr.id_reparacion = rr.id_reparacion WHERE tr.id_empleado = t.id_empleado AND rr.id_repuestos = rp.id_repuestos));

-- 42. Obtener las sedes que han recibido todos los repuestos de la categoría ELECTRICO.
SELECT s.id_sucursal, s.nombre FROM sucursal s WHERE NOT EXISTS (SELECT 1 FROM repuesto rp WHERE rp.categoria = 'ELECTRICO' AND NOT EXISTS (SELECT 1 FROM suministros su WHERE su.id_sucursal = s.id_sucursal AND su.id_repuestos = rp.id_repuestos));

-- 43. Obtener las sedes que tienen motos de todas las marcas japonesas.
SELECT s.id_sucursal, s.nombre FROM sucursal s WHERE NOT EXISTS (SELECT 1 FROM marca ma WHERE ma.pais_origen = 'Japón' AND NOT EXISTS (SELECT 1 FROM moto m WHERE m.id_sucursal = s.id_sucursal AND m.id_marca = ma.id_marca));

-- 44. Obtener el número de motos de cada sede por estado.
SELECT s.nombre, m.estado, COUNT(m.placa) AS total FROM moto m JOIN sucursal s ON m.id_sucursal = s.id_sucursal GROUP BY s.nombre, m.estado ORDER BY s.nombre, m.estado;

-- 45. Obtener cuántas motos ha vendido cada vendedor y el valor total vendido.
SELECT id_vendedor, COUNT(placa) AS ventas, SUM(valor_total) AS total FROM venta_moto GROUP BY id_vendedor ORDER BY id_vendedor;

-- 46. Calcular el valor de cada venta de accesorios a partir de sus productos.
SELECT id_venta, SUM(cantidad * precio_unitario) AS calculado FROM detalle_accesorio GROUP BY id_venta ORDER BY id_venta;

-- 47. Obtener el costo en repuestos de cada reparación, incluidas las que no usaron repuestos.
SELECT r.id_reparacion, r.placa, r.costo, t.repuestos FROM reparacion r LEFT JOIN (SELECT id_reparacion, SUM(cantidad * precio_unitario) AS repuestos FROM reparacion_repuestos GROUP BY id_reparacion) t ON r.id_reparacion = t.id_reparacion ORDER BY r.id_reparacion;

-- 48. Obtener el precio promedio, el mínimo y el máximo de las motos por tipo de moto.
SELECT mo.tipo_moto, ROUND(AVG(m.precio)) AS promedio, MIN(m.precio) AS minimo, MAX(m.precio) AS maximo FROM moto m JOIN modelo mo ON m.id_marca = mo.id_marca AND m.id_modelo = mo.id_modelo GROUP BY mo.tipo_moto ORDER BY mo.tipo_moto;

-- 49. Obtener la marca con más motos vendidas.
SELECT ma.nombre, COUNT(*) AS vendidas FROM venta_moto v JOIN moto m ON v.placa = m.placa JOIN marca ma ON m.id_marca = ma.id_marca GROUP BY ma.nombre HAVING COUNT(*) = (SELECT MAX(n) FROM (SELECT COUNT(*) AS n FROM venta_moto v2 JOIN moto m2 ON v2.placa = m2.placa GROUP BY m2.id_marca) t);

-- 50. Obtener cuántas reparaciones se han hecho en cada sede.
SELECT s.nombre, COUNT(DISTINCT tr.id_reparacion) AS reparaciones FROM tecnicos_reparacion tr JOIN empleado e ON tr.id_empleado = e.id_empleado JOIN sucursal s ON e.id_sucursal = s.id_sucursal GROUP BY s.nombre ORDER BY s.nombre;
