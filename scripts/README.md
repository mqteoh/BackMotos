# Scripts de la base de datos

Scripts de PostgreSQL 13 del concesionario de motos, según el modelo oficial de la Fase 1. Son la fuente de verdad del modelo: el back los valida al arrancar y no crea tablas por su cuenta.

Todo vive en el esquema `concesionario_motos` de la base `motomaster`, en la máquina virtual del equipo. Los scripts no tocan el esquema `public`, que ya tenía las tablas de otro proyecto.

## Orden de ejecución

| Script | Contenido |
|---|---|
| `01_creacion.sql` | Borra y crea el esquema `concesionario_motos` con las 21 tablas del modelo, más `cuenta` y `reserva`, que usa la web. Incluye llaves, restricciones `CHECK` y 10 triggers de reglas de negocio |
| `02_insercion.sql` | Datos iniciales: sedes, marcas, modelos, motos, empleados, cuentas, clientes, reservas, ventas, reparaciones, repuestos y suministros |
| `03_varios.sql` | Vistas (`v_moto_disponible`, `v_sede_reparacion`, `v_costo_reparacion`, `v_ventas_por_vendedor`, `v_repuestos_por_sucursal`) y funciones (`sp_vencer_reservas()`, `sp_historial_moto(placa)`) |
| `04_usuarios.sql` | Roles `app_motos` para el back y `consulta_motos` de solo lectura. Lo ejecuta el usuario `postgres` |
| `05_consultas_algebra.sql` | Las 50 consultas del documento de álgebra relacional; no modifica datos |

Con psql:

```bash
psql -h 100.122.190.84 -U app_user -d motomaster -f 01_creacion.sql
```

Se repite con 02 y 03. El 04 se ejecuta en la VM con `sudo -u postgres psql -d motomaster -f 04_usuarios.sql`.

## Reglas que cumple la base

- El estado de la moto (`DISPONIBLE`, `RESERVADA`, `VENDIDA`) lo cambian los triggers de reservas y ventas, no la aplicación.
- Solo se reservan motos disponibles, y una moto reservada no se le vende a otro cliente.
- El taller solo registra reparaciones de motos vendidas, con fecha igual o posterior a la venta. Los técnicos de una reparación deben ser de la misma sede.
- El valor total de una venta de accesorios lo calcula un trigger a partir de sus detalles.

Los triggers rechazan las operaciones con `RAISE EXCEPTION ... USING ERRCODE = '45000'`, y el back muestra ese mensaje al usuario.

## Antes de usar en la VM

En `04_usuarios.sql`, reemplazar `CAMBIAR_ESTA_CLAVE` por contraseñas propias. Esas son las que van en la variable `DB_PASSWORD` del back.

## Esquema de pruebas

Las pruebas del back usan `concesionario_motos_test`. Se crea con los scripts 01 a 03 reemplazando `concesionario_motos` por `concesionario_motos_test`. Varias pruebas esperan exactamente los datos de `02_insercion.sql`, así que conviene recrear este esquema antes de ejecutar `mvnw test`.

## Usuarios de la aplicación

`02_insercion.sql` guarda las contraseñas de la aplicación ya cifradas con BCrypt en la tabla `cuenta`. Para pruebas, los empleados entran con `Motos2026*` y los clientes con `Cliente2026*`. Por ejemplo:

- Dueño: ricardo.salazar@concesionario-motos.test
- Vendedora: laura.gomez@concesionario-motos.test
- Técnico: jhon.castano@concesionario-motos.test
- Clienta: maria.ramirez@correo.test
