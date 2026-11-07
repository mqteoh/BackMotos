SET search_path TO concesionario_motos;

INSERT INTO marca (nombre, pais_origen) VALUES
    ('Yamaha', 'Japón'),
    ('Honda', 'Japón'),
    ('Suzuki', 'Japón'),
    ('Bajaj', 'India'),
    ('AKT', 'Colombia'),
    ('KTM', 'Austria'),
    ('Kawasaki', 'Japón'),
    ('TVS', 'India');

INSERT INTO modelo (id_marca, id_modelo, nombre, tipo_moto, cilindraje, potencia, capacidad_tanque) VALUES
    (1, 1, 'FZ 2.0', 'NAKED', 149, 12.2, 12.0),
    (1, 2, 'NMAX 155', 'SCOOTER', 155, 15.1, 7.1),
    (1, 3, 'MT-03', 'NAKED', 321, 41.4, 14.0),
    (1, 4, 'XTZ 150', 'DOBLE_PROPOSITO', 149, 12.0, 10.2),
    (2, 1, 'CB 190R', 'NAKED', 184, 15.5, 12.0),
    (2, 2, 'XR 150L', 'DOBLE_PROPOSITO', 149, 12.5, 12.0),
    (2, 3, 'Navi', 'SCOOTER', 109, 7.8, 3.5),
    (3, 1, 'Gixxer 150', 'NAKED', 155, 13.4, 12.0),
    (3, 2, 'V-Strom 250 SX', 'TOURING', 249, 26.0, 12.0),
    (4, 1, 'Pulsar NS 200', 'NAKED', 199, 24.5, 12.0),
    (4, 2, 'Boxer CT 100', 'CALLE', 102, 7.8, 10.5),
    (5, 1, 'NKD 125', 'CALLE', 125, 9.5, 11.0),
    (5, 2, 'TT 200 DS', 'DOBLE_PROPOSITO', 196, 16.0, 13.0),
    (6, 1, 'Duke 200', 'NAKED', 199, 25.0, 13.4),
    (6, 2, '390 Adventure', 'TOURING', 373, 43.0, 14.5),
    (7, 1, 'Ninja 400', 'DEPORTIVA', 399, 45.0, 14.0),
    (8, 1, 'Apache RTR 160 4V', 'NAKED', 160, 17.3, 12.0);

INSERT INTO sucursal (nombre, ciudad, barrio, calle) VALUES
    ('Sede Chapinero', 'Bogotá', 'Chapinero', 'Calle 63 # 13-45'),
    ('Sede Suba', 'Bogotá', 'Suba Centro', 'Avenida Calle 145 # 91-19'),
    ('Sede Laureles', 'Medellín', 'Laureles', 'Carrera 70 # 44-12'),
    ('Sede Granada', 'Cali', 'Granada', 'Avenida 6 Norte # 15-40');

INSERT INTO empleado (documento, nombres, apellidos, fecha_contratacion, salario, id_sucursal, id_jefe) VALUES
    ('79456123', 'Ricardo', 'Salazar Mejía', '2015-02-01', 9500000, 1, NULL),
    ('52789456', 'Laura', 'Gómez Torres', '2018-03-12', 3200000, 1, NULL),
    ('1018456789', 'Andrés', 'Rincón Pardo', '2021-07-05', 2400000, 1, NULL),
    ('80123987', 'Jhon', 'Castaño Ruiz', '2019-01-21', 2900000, 1, NULL),
    ('1020789654', 'Felipe', 'Ortiz Lozano', '2022-09-15', 2300000, 1, NULL),
    ('1032456987', 'Camila', 'Herrera Díaz', '2020-04-02', 3000000, 2, NULL),
    ('79987654', 'Diego', 'Moreno Vargas', '2017-11-30', 3100000, 2, NULL),
    ('71654321', 'Santiago', 'Pérez Arango', '2019-06-17', 3000000, 3, NULL),
    ('98765432', 'Mauricio', 'Restrepo Gil', '2018-08-08', 3000000, 3, NULL),
    ('66987123', 'Valentina', 'Cruz Ospina', '2021-02-22', 2900000, 4, NULL),
    ('94321765', 'Óscar', 'Valencia Muñoz', '2020-10-10', 2800000, 4, NULL),
    ('1019876543', 'Natalia', 'Rojas Beltrán', '2023-05-03', 2200000, 2, NULL);

UPDATE empleado SET id_jefe = 1 WHERE id_empleado = 2;
UPDATE empleado SET id_jefe = 2 WHERE id_empleado = 3;
UPDATE empleado SET id_jefe = 2 WHERE id_empleado = 4;
UPDATE empleado SET id_jefe = 2 WHERE id_empleado = 5;
UPDATE empleado SET id_jefe = 1 WHERE id_empleado = 6;
UPDATE empleado SET id_jefe = 6 WHERE id_empleado = 7;
UPDATE empleado SET id_jefe = 1 WHERE id_empleado = 8;
UPDATE empleado SET id_jefe = 8 WHERE id_empleado = 9;
UPDATE empleado SET id_jefe = 1 WHERE id_empleado = 10;
UPDATE empleado SET id_jefe = 10 WHERE id_empleado = 11;
UPDATE empleado SET id_jefe = 6 WHERE id_empleado = 12;

INSERT INTO telefono_empleado (id_empleado, numero) VALUES
    (1, '3104567890'),
    (2, '3115678901'),
    (2, '6017654321'),
    (3, '3126789012'),
    (4, '3137890123'),
    (5, '3148901234'),
    (6, '3159012345'),
    (7, '3160123456'),
    (8, '3171234567'),
    (8, '6044123456'),
    (9, '3182345678'),
    (10, '3193456789'),
    (11, '3204567890'),
    (12, '3215678901');

INSERT INTO vendedor (id_empleado, certificado_ventas, comision) VALUES
    (2, 'Asesor comercial certificado', 3.0),
    (3, 'Técnico en ventas', 2.0),
    (6, 'Asesor comercial certificado', 2.5),
    (8, 'Tecnólogo en gestión comercial', 2.5),
    (10, 'Técnico en ventas', 2.0),
    (12, 'Técnico en ventas', 1.5);

INSERT INTO tecnico (id_empleado, certificado_tecnico, especialidad) VALUES
    (4, 'Técnico en mantenimiento de motocicletas', 'Motor'),
    (5, 'Técnico en mantenimiento de motocicletas', 'Sistema eléctrico'),
    (7, 'Tecnólogo en mecánica automotriz', 'Frenos y suspensión'),
    (9, 'Técnico en mantenimiento de motocicletas', 'Inyección electrónica'),
    (11, 'Técnico en mantenimiento de motocicletas', 'Transmisión');

INSERT INTO cliente (documento, nombres, apellidos, fecha_nacimiento, ciudad, barrio, calle, numero_direccion) VALUES
    ('1013654789', 'María José', 'Ramírez Castro', '1995-04-18', 'Bogotá', 'Galerías', 'Calle 53', '24-31'),
    ('1022345678', 'Juan Sebastián', 'López Medina', '1998-11-02', 'Bogotá', 'Cedritos', 'Calle 140', '11-50'),
    ('80765432', 'Carlos Alberto', 'Martínez Rueda', '1982-07-25', 'Bogotá', 'Suba Rincón', 'Carrera 92', '130-15'),
    ('1036987456', 'Daniela', 'Zapata Henao', '2000-01-30', 'Medellín', 'Belén', 'Calle 30A', '76-20'),
    ('71234890', 'Jorge Iván', 'Echeverri Mesa', '1979-09-14', 'Medellín', 'Laureles', 'Circular 4', '70-11'),
    ('1144098765', 'Luisa Fernanda', 'Cárdenas Ríos', '1997-06-06', 'Cali', 'San Fernando', 'Calle 5', '36-08'),
    ('16789234', 'Hernán', 'Quintero Lasso', '1985-12-19', 'Cali', 'Granada', 'Calle 15 Norte', '8-44'),
    ('1015478963', 'Paula Andrea', 'Sánchez Niño', '1993-03-03', 'Bogotá', 'Chapinero Alto', 'Calle 61', '3-20'),
    ('1001234567', 'Mateo', 'Gutiérrez Bernal', '2003-08-21', 'Bogotá', 'Kennedy', 'Avenida 1 de Mayo', '68-90'),
    ('43567890', 'Gloria Patricia', 'Vélez Ochoa', '1976-02-11', 'Medellín', 'Envigado Centro', 'Carrera 43A', '38-05'),
    ('1030567123', 'Kevin', 'Arias Pinzón', '1999-10-27', 'Bogotá', 'Fontibón', 'Calle 22', '100-31'),
    ('94567321', 'Fabio', 'Montoya Lenis', '1981-05-09', 'Cali', 'Ciudad Jardín', 'Carrera 105', '14-60'),
    ('1128456789', 'Sara', 'Londoño Isaza', '2001-12-01', 'Medellín', 'Robledo', 'Calle 65', '88-12'),
    ('79345678', 'Wilson', 'Peña Garzón', '1977-08-16', 'Bogotá', 'Bosa', 'Calle 59 Sur', '80-14'),
    ('1107654321', 'Natalia', 'Mosquera Rivas', '1996-04-24', 'Cali', 'El Ingenio', 'Carrera 85', '16-30');

INSERT INTO telefono_cliente (id_cliente, numero) VALUES
    (1, '3001234567'),
    (2, '3012345678'),
    (2, '6016543210'),
    (3, '3023456789'),
    (4, '3034567890'),
    (5, '3045678901'),
    (5, '6043456789'),
    (6, '3056789012'),
    (7, '3067890123'),
    (8, '3078901234'),
    (9, '3089012345'),
    (10, '3090123456'),
    (11, '3101112233'),
    (12, '3112223344'),
    (13, '3123334455'),
    (14, '3134445566'),
    (15, '3145556677'),
    (15, '6023334455');

INSERT INTO correo_cliente (direccion_correo, id_cliente) VALUES
    ('maria.ramirez@correo.test', 1),
    ('juan.lopez@correo.test', 2),
    ('carlos.martinez@correo.test', 3),
    ('carlos.martinez.trabajo@empresa.test', 3),
    ('daniela.zapata@correo.test', 4),
    ('jorge.echeverri@correo.test', 5),
    ('luisa.cardenas@correo.test', 6),
    ('luisa.cardenas.trabajo@empresa.test', 6),
    ('hernan.quintero@correo.test', 7),
    ('paula.sanchez@correo.test', 8),
    ('mateo.gutierrez@correo.test', 9),
    ('mateo.gutierrez.trabajo@empresa.test', 9),
    ('gloria.velez@correo.test', 10),
    ('kevin.arias@correo.test', 11),
    ('fabio.montoya@correo.test', 12),
    ('fabio.montoya.trabajo@empresa.test', 12),
    ('sara.londono@correo.test', 13),
    ('wilson.pena@correo.test', 14),
    ('natalia.mosquera@correo.test', 15),
    ('natalia.mosquera.trabajo@empresa.test', 15);

INSERT INTO cuenta (correo, password_hash, rol, id_cliente, id_empleado) VALUES
    ('ricardo.salazar@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'DUENO', NULL, 1),
    ('laura.gomez@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 2),
    ('andres.rincon@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 3),
    ('jhon.castano@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'TECNICO', NULL, 4),
    ('felipe.ortiz@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'TECNICO', NULL, 5),
    ('camila.herrera@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 6),
    ('diego.moreno@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'TECNICO', NULL, 7),
    ('santiago.perez@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 8),
    ('mauricio.restrepo@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'TECNICO', NULL, 9),
    ('valentina.cruz@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 10),
    ('oscar.valencia@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'TECNICO', NULL, 11),
    ('natalia.rojas@concesionario-motos.test', '$2b$10$MiYldLIR/ak19GOKp5xtLOLhYeURQsm/Gs.P29N93e2ePgJlv2mMO', 'VENDEDOR', NULL, 12),
    ('maria.ramirez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 1, NULL),
    ('juan.lopez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 2, NULL),
    ('carlos.martinez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 3, NULL),
    ('daniela.zapata@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 4, NULL),
    ('jorge.echeverri@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 5, NULL),
    ('luisa.cardenas@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 6, NULL),
    ('hernan.quintero@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 7, NULL),
    ('paula.sanchez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 8, NULL),
    ('mateo.gutierrez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 9, NULL),
    ('gloria.velez@correo.test', '$2b$10$aWEXO/xWJrlJPo1fNdREs.CDsT20BmJpcMJlL4Gv5.4X2oK51wv0.', 'CLIENTE', 10, NULL);

INSERT INTO moto (placa, id_marca, id_modelo, id_sucursal, color, anio, precio, kilometraje, fecha_ingreso) VALUES
    ('GTK21F', 1, 1, 1, 'Azul', 2026, 10900000, 5200, '2026-01-10'),
    ('GTK22F', 1, 2, 1, 'Gris mate', 2026, 15900000, 0, '2026-01-10'),
    ('HBN45C', 1, 3, 1, 'Negro', 2026, 27900000, 3100, '2026-02-03'),
    ('HBN46C', 2, 1, 1, 'Rojo', 2026, 11500000, 2600, '2026-02-03'),
    ('JKL78D', 3, 1, 1, 'Azul', 2025, 10500000, 9800, '2025-09-15'),
    ('JKL79D', 4, 1, 1, 'Blanco', 2026, 12400000, 0, '2026-03-20'),
    ('MNP12E', 6, 1, 1, 'Naranja', 2026, 17500000, 0, '2026-04-11'),
    ('MNP13E', 7, 1, 1, 'Verde', 2026, 32500000, 0, '2026-04-11'),
    ('QRS34A', 1, 1, 2, 'Negro', 2026, 10900000, 0, '2026-01-25'),
    ('QRS35A', 2, 2, 2, 'Rojo', 2025, 10200000, 0, '2025-08-30'),
    ('QRS36A', 2, 3, 2, 'Blanco', 2026, 6900000, 1800, '2026-02-14'),
    ('TUV56B', 4, 2, 2, 'Negro', 2026, 5600000, 4300, '2026-03-01'),
    ('TUV57B', 5, 1, 2, 'Rojo', 2026, 5900000, 0, '2026-03-01'),
    ('TUV58B', 8, 1, 2, 'Gris', 2026, 9800000, 1200, '2026-05-05'),
    ('WXY90G', 1, 4, 3, 'Azul', 2026, 12900000, 0, '2026-02-18'),
    ('WXY91G', 2, 2, 3, 'Rojo', 2026, 10200000, 0, '2026-02-18'),
    ('WXY92G', 3, 2, 3, 'Amarillo', 2026, 19500000, 6700, '2026-03-27'),
    ('ZAB11H', 5, 2, 3, 'Negro', 2026, 9500000, 0, '2026-04-02'),
    ('ZAB12H', 6, 2, 3, 'Naranja', 2026, 33900000, 900, '2026-06-12'),
    ('ZAB13H', 4, 1, 3, 'Azul', 2025, 12400000, 7400, '2025-10-04'),
    ('CDE33J', 1, 2, 4, 'Azul', 2026, 15900000, 4100, '2026-01-30'),
    ('CDE34J', 2, 1, 4, 'Negro', 2026, 11500000, 3000, '2026-01-30'),
    ('CDE35J', 4, 2, 4, 'Rojo', 2026, 5600000, 0, '2026-03-15'),
    ('FGH67K', 5, 1, 4, 'Blanco', 2026, 5900000, 800, '2026-04-20'),
    ('FGH68K', 8, 1, 4, 'Negro', 2026, 9800000, 0, '2026-05-18'),
    ('FGH69K', 7, 1, 4, 'Verde', 2026, 32500000, 0, '2026-07-07'),
    ('LMN24P', 1, 3, 2, 'Azul', 2026, 27900000, 0, '2026-08-01'),
    ('LMN25P', 6, 1, 4, 'Naranja', 2026, 17500000, 0, '2026-08-09');

INSERT INTO reserva (placa, id_cliente, fecha_reserva, fecha_vencimiento, estado) VALUES
    ('HBN46C', 1, '2026-02-20 10:15:00', '2026-02-27', 'ACTIVA'),
    ('QRS36A', 2, '2026-03-02 16:40:00', '2026-03-09', 'ACTIVA'),
    ('WXY92G', 4, '2026-04-05 09:05:00', '2026-04-12', 'ACTIVA'),
    ('TUV58B', 9, '2026-05-11 11:30:00', '2026-05-18', 'CANCELADA'),
    ('ZAB12H', 5, '2026-06-01 14:00:00', '2026-06-08', 'VENCIDA'),
    ('LMN24P', 8, '2026-10-24 18:20:00', '2026-10-31', 'ACTIVA'),
    ('FGH69K', 6, '2026-10-26 12:10:00', '2026-11-02', 'ACTIVA'),
    ('MNP12E', 2, '2026-10-27 09:45:00', '2026-11-03', 'ACTIVA');

INSERT INTO venta_moto (placa, id_cliente, id_vendedor, fecha_venta, metodo_pago, tipo_factura, valor_total) VALUES
    ('JKL78D', 3, 2, '2025-10-20 11:00:00', 'TRANSFERENCIA', 'ELECTRONICA', 10500000),
    ('ZAB13H', 5, 8, '2025-11-14 15:30:00', 'TARJETA_CREDITO', 'ELECTRONICA', 12100000),
    ('GTK21F', 9, 3, '2026-02-05 10:20:00', 'EFECTIVO', 'POS', 10700000),
    ('HBN46C', 1, 2, '2026-02-24 12:00:00', 'TRANSFERENCIA', 'ELECTRONICA', 11300000),
    ('QRS36A', 2, 6, '2026-03-06 17:10:00', 'TARJETA_DEBITO', 'ELECTRONICA', 6800000),
    ('CDE33J', 6, 10, '2026-03-10 09:40:00', 'TARJETA_CREDITO', 'ELECTRONICA', 15900000),
    ('WXY92G', 4, 8, '2026-04-08 16:25:00', 'TRANSFERENCIA', 'ELECTRONICA', 19000000),
    ('TUV56B', 11, 12, '2026-04-22 13:15:00', 'EFECTIVO', 'POS', 5500000),
    ('CDE34J', 7, 10, '2026-05-02 10:05:00', 'TARJETA_CREDITO', 'ELECTRONICA', 11400000),
    ('HBN45C', 8, 2, '2026-05-30 11:50:00', 'TRANSFERENCIA', 'ELECTRONICA', 27500000),
    ('TUV58B', 14, 6, '2026-06-18 15:00:00', 'TARJETA_DEBITO', 'POS', 9700000),
    ('ZAB12H', 10, 8, '2026-07-25 10:30:00', 'TRANSFERENCIA', 'ELECTRONICA', 33500000),
    ('FGH67K', 15, 10, '2026-09-12 16:45:00', 'TARJETA_CREDITO', 'ELECTRONICA', 5850000);

INSERT INTO accesorio (nombre, marca, descripcion, precio) VALUES
    ('Casco integral Shaft Pro 610', 'Shaft', 'Casco integral certificado DOT y NTC', 389000),
    ('Guantes Shaft Racing', 'Shaft', 'Guantes con protección en nudillos', 129000),
    ('Chaqueta HRO Urban', 'HRO', 'Chaqueta textil con protecciones', 459000),
    ('Impermeable HRO dos piezas', 'HRO', 'Traje impermeable con reflectivos', 189000),
    ('Casco abatible Ich 3110', 'Ich', 'Casco abatible con visor interno', 349000),
    ('Intercomunicador Sena 5S', 'Sena', 'Intercomunicador Bluetooth para casco', 699000),
    ('Baúl Givi E43', 'Givi', 'Baúl trasero de 43 litros', 589000),
    ('Candado de disco Xena XX6', 'Xena', 'Candado con alarma de 120 dB', 299000),
    ('Protector de tanque Shaft', 'Shaft', 'Adhesivo protector de tanque', 59000),
    ('Soporte de celular Givi', 'Givi', 'Soporte para manubrio con cargador USB', 159000);

INSERT INTO venta_accesorio (id_cliente, id_vendedor, fecha_venta, metodo_pago, tipo_factura) VALUES
    (3, 2, '2025-10-20 11:30:00', 'TRANSFERENCIA', 'ELECTRONICA'),
    (5, 8, '2025-11-14 16:00:00', 'TARJETA_CREDITO', 'ELECTRONICA'),
    (12, 10, '2026-01-18 10:00:00', 'EFECTIVO', 'POS'),
    (9, 3, '2026-02-05 10:45:00', 'EFECTIVO', 'POS'),
    (1, 2, '2026-02-24 12:30:00', 'TRANSFERENCIA', 'ELECTRONICA'),
    (13, 8, '2026-03-11 15:20:00', 'TARJETA_DEBITO', 'POS'),
    (4, 8, '2026-04-08 17:00:00', 'TRANSFERENCIA', 'ELECTRONICA'),
    (7, 10, '2026-05-02 10:30:00', 'TARJETA_CREDITO', 'ELECTRONICA'),
    (8, 2, '2026-05-30 12:20:00', 'TRANSFERENCIA', 'ELECTRONICA'),
    (2, 6, '2026-07-03 09:15:00', 'EFECTIVO', 'POS'),
    (3, 3, '2026-08-19 18:05:00', 'TARJETA_DEBITO', 'POS'),
    (10, 8, '2026-10-02 11:40:00', 'TARJETA_CREDITO', 'ELECTRONICA');

INSERT INTO detalle_accesorio (id_venta, id_accesorio, cantidad, precio_unitario) VALUES
    (1, 1, 1, 389000),
    (1, 2, 1, 129000),
    (1, 9, 1, 59000),
    (2, 5, 1, 349000),
    (3, 4, 1, 189000),
    (3, 8, 1, 299000),
    (4, 1, 1, 379000),
    (4, 4, 1, 189000),
    (5, 5, 1, 349000),
    (5, 6, 1, 699000),
    (6, 10, 1, 159000),
    (7, 7, 1, 589000),
    (7, 3, 1, 459000),
    (8, 1, 2, 389000),
    (9, 6, 1, 699000),
    (9, 2, 1, 129000),
    (10, 9, 2, 59000),
    (10, 10, 1, 159000),
    (11, 3, 1, 449000),
    (12, 7, 1, 589000),
    (12, 8, 1, 299000),
    (12, 6, 1, 699000);

INSERT INTO proveedor (nit, nombre, telefono, correo) VALUES
    ('900456789-1', 'Distribuidora Andina de Repuestos S.A.S.', '6014567890', 'pedidos@andinarepuestos.test'),
    ('901234567-3', 'Lubricantes del Pacífico S.A.S.', '6023456789', 'ventas@lubripacifico.test'),
    ('830987654-2', 'Moto Partes Antioquia Ltda.', '6045678901', 'compras@motopartesantioquia.test'),
    ('900111222-5', 'Importadora Llantas y Frenos S.A.', '6017778899', 'pedidos@llantasfrenos.test');

INSERT INTO repuesto (nombre, categoria, precio) VALUES
    ('Aceite 20W-50 semisintético 1L', 'LUBRICANTES', 38000),
    ('Filtro de aceite', 'MOTOR', 22000),
    ('Pastillas de freno delanteras', 'FRENOS', 65000),
    ('Pastillas de freno traseras', 'FRENOS', 58000),
    ('Kit de arrastre', 'TRANSMISION', 245000),
    ('Bujía iridio', 'ELECTRICO', 48000),
    ('Batería 12V 7Ah', 'ELECTRICO', 189000),
    ('Llanta trasera 130/70-17', 'LLANTAS', 310000),
    ('Cable de clutch', 'TRANSMISION', 35000),
    ('Bombillo farola LED', 'ELECTRICO', 72000),
    ('Amortiguador trasero', 'SUSPENSION', 420000),
    ('Líquido de frenos DOT 4', 'FRENOS', 28000);

INSERT INTO suministros (nit, id_repuestos, id_sucursal, fecha_suministro, cantidad, costo_unitario) VALUES
    ('900456789-1', 2, 1, '2026-01-08 08:30:00', 40, 12500),
    ('900456789-1', 2, 2, '2026-01-09 09:00:00', 30, 12500),
    ('900456789-1', 2, 3, '2026-01-12 10:15:00', 30, 12800),
    ('900456789-1', 2, 4, '2026-01-13 11:00:00', 25, 12800),
    ('900456789-1', 5, 1, '2026-02-02 08:45:00', 10, 168000),
    ('900456789-1', 6, 3, '2026-02-20 14:30:00', 20, 31000),
    ('900456789-1', 9, 4, '2026-03-04 09:20:00', 15, 21000),
    ('900456789-1', 2, 1, '2026-06-10 08:30:00', 40, 13100),
    ('901234567-3', 1, 1, '2026-01-15 07:50:00', 120, 24000),
    ('901234567-3', 1, 4, '2026-01-16 08:10:00', 80, 24000),
    ('901234567-3', 1, 2, '2026-04-03 08:00:00', 100, 24500),
    ('901234567-3', 12, 4, '2026-04-03 08:00:00', 30, 17000),
    ('830987654-2', 7, 3, '2026-02-11 13:00:00', 12, 132000),
    ('830987654-2', 10, 3, '2026-03-19 15:45:00', 20, 45000),
    ('830987654-2', 11, 3, '2026-05-07 10:30:00', 6, 290000),
    ('830987654-2', 5, 3, '2026-06-21 09:00:00', 8, 171000),
    ('900111222-5', 3, 1, '2026-01-22 10:00:00', 40, 39000),
    ('900111222-5', 4, 2, '2026-01-23 10:30:00', 30, 35000),
    ('900111222-5', 8, 2, '2026-03-12 11:15:00', 10, 228000),
    ('900111222-5', 3, 4, '2026-07-14 09:40:00', 25, 40500),
    ('900111222-5', 12, 1, '2026-07-14 09:40:00', 40, 16500);

INSERT INTO reparacion (placa, fecha, descripcion, costo) VALUES
    ('JKL78D', '2025-12-15', 'Mantenimiento de los 1000 km: cambio de aceite y ajuste de cadena', 80000),
    ('ZAB13H', '2026-03-20', 'Mantenimiento de los 1000 km', 80000),
    ('HBN46C', '2026-04-10', 'Ruido en transmisión y frenos delanteros gastados', 150000),
    ('QRS36A', '2026-05-15', 'La moto no enciende en frío', 90000),
    ('WXY92G', '2026-06-20', 'Cambio de llanta trasera y revisión de suspensión', 70000),
    ('GTK21F', '2026-07-02', 'Mantenimiento de los 5000 km y cambio de pastillas', 120000),
    ('CDE34J', '2026-08-12', 'Mantenimiento de los 1000 km', 80000),
    ('CDE33J', '2026-09-25', 'Revisión de frenos completos', 60000),
    ('HBN45C', '2026-10-15', 'Falla intermitente en luces', 110000),
    ('TUV56B', '2026-10-20', 'Ajuste de cadena y revisión general', 50000);

INSERT INTO tecnicos_reparacion (id_reparacion, id_empleado) VALUES
    (1, 4),
    (2, 9),
    (3, 4),
    (3, 5),
    (4, 7),
    (5, 9),
    (6, 4),
    (7, 11),
    (8, 11),
    (9, 4),
    (9, 5),
    (10, 7);

INSERT INTO reparacion_repuestos (id_reparacion, id_repuestos, cantidad, precio_unitario) VALUES
    (1, 1, 1, 38000),
    (1, 2, 1, 22000),
    (2, 1, 1, 38000),
    (2, 2, 1, 22000),
    (3, 5, 1, 245000),
    (3, 3, 1, 65000),
    (3, 12, 1, 28000),
    (4, 6, 1, 48000),
    (4, 7, 1, 189000),
    (5, 8, 1, 310000),
    (5, 11, 1, 420000),
    (6, 1, 1, 38000),
    (6, 2, 1, 22000),
    (6, 3, 1, 65000),
    (6, 4, 1, 58000),
    (7, 1, 1, 38000),
    (8, 3, 1, 65000),
    (8, 4, 1, 58000),
    (8, 12, 1, 28000),
    (9, 10, 1, 72000);
