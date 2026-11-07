# BackMotos

API REST del sistema de gestión de un concesionario de motos. Proyecto final de Bases de Datos 1, Ingeniería de Sistemas, Universidad El Bosque.

El sistema tiene una vitrina pública de motos y cuatro roles con su propia área:

| Rol | Qué hace |
|---|---|
| Cliente | Reserva motos y consulta sus compras y las reparaciones de sus motos |
| Vendedor | Atiende las reservas de su sede, registra clientes y vende motos y accesorios |
| Técnico | Registra y corrige las reparaciones hechas, con los técnicos que participaron y los repuestos usados |
| Dueño | Administra las 14 tablas del negocio y consulta los reportes de gestión |

El front en Angular está en el repositorio [FrontMotos](https://github.com/mqteoh/FrontMotos). En producción se empaqueta dentro de este WAR, así que el sistema completo es un solo archivo.

## Tecnologías

- Java 21 y Spring Boot 3.3.5 (web, data JPA, validación, seguridad)
- PostgreSQL 13 con los scripts SQL como fuente de verdad (`ddl-auto=validate`)
- Autenticación con JWT y contraseñas en BCrypt
- Documentación con springdoc OpenAPI
- Pruebas con JUnit 5, Mockito y MockMvc

## Requisitos

- JDK 21
- PostgreSQL 13 con el esquema creado por los scripts de la carpeta `scripts/` de este repositorio: se ejecutan en orden de `01_creacion.sql` a `04_usuarios.sql` (ver `scripts/README.md`)
- Node.js y npm, solo para compilar el front antes de empaquetar

## Variables de entorno

El back no guarda credenciales en el código. Se leen del entorno o de un archivo `.env` en la raíz del proyecto, que está en `.gitignore`.

| Variable | Obligatoria | Uso |
|---|---|---|
| `DB_URL` | Sí | Por ejemplo `jdbc:postgresql://servidor:5432/motomaster?currentSchema=concesionario_motos` |
| `DB_USERNAME` | Sí | Usuario de PostgreSQL (el script 04 crea `app_motos`) |
| `DB_PASSWORD` | Sí | Contraseña de ese usuario |
| `JWT_SECRET` | Sí | Clave de firma de los tokens, de al menos 32 caracteres |
| `JWT_EXPIRATION` | No | Duración del token en milisegundos; por defecto 28800000 (8 horas) |
| `CORS_ORIGENES` | No | Orígenes permitidos separados por comas; por defecto `http://localhost:4200` para el desarrollo del front |

## Compilar con el front incluido

1. Compilar el front en su repositorio, que debe estar en `../../Front` respecto a este proyecto:

```bash
npm ci
```

```bash
npx ng build
```

2. Empaquetar el back. Maven copia `../../Front/dist/Front/browser` dentro del WAR:

```bash
./mvnw clean package -DskipTests
```

Si el front está en otra ruta, se indica con `-Dfront.dist=<ruta a dist/Front/browser>`. Si no existe el build, el WAR se genera igual pero sin front.

El resultado es `target/proyectoFinal-0.0.1-SNAPSHOT.war`.

## Ejecutar

Con las variables de entorno definidas:

```bash
java -jar target/proyectoFinal-0.0.1-SNAPSHOT.war
```

| Dirección | Contenido |
|---|---|
| `http://localhost:8080/` | Aplicación web |
| `http://localhost:8080/docs` | Swagger UI con todos los endpoints por rol |
| `/api/publico/**` | Catálogo, accesorios y sedes, sin sesión |
| `/api/cliente/**` | Área del cliente |
| `/api/vendedor/**` | Área del vendedor (el dueño también entra) |
| `/api/tecnico/**` | Área del técnico (el dueño también entra) |
| `/api/admin/**` | Administración y reportes del dueño |
| `/auth/**` | Inicio de sesión, registro de clientes y usuario actual |

Cualquier otra ruta sin extensión devuelve el `index.html` del front, para que los enlaces directos y la recarga funcionen.

## Usuarios de prueba

Los crea `02_insercion.sql`. Son solo para desarrollo y pruebas.

| Rol | Correo | Contraseña |
|---|---|---|
| Dueño | ricardo.salazar@concesionario-motos.test | Motos2026* |
| Vendedora (sede 1) | laura.gomez@concesionario-motos.test | Motos2026* |
| Técnico (sede 1) | jhon.castano@concesionario-motos.test | Motos2026* |
| Clienta | maria.ramirez@correo.test | Cliente2026* |

## Pruebas

Las pruebas usan un esquema aparte, `concesionario_motos_test`, creado con los mismos scripts reemplazando el nombre del esquema. Muchas pruebas esperan exactamente los datos iniciales, así que conviene recrear ese esquema antes de ejecutarlas. Hoy pasan las 119.

Variables: `TEST_DB_URL` (con `?currentSchema=concesionario_motos_test`), `TEST_DB_USERNAME` y `TEST_DB_PASSWORD`.

```bash
./mvnw test
```

## Estructura

```
src/main/java/co/edu/unbosque/BackMotos
├── configuration   Arranque, reloj, OpenAPI, tareas programadas y servicio del front
├── controller      Endpoints por rol y CRUD genérico del dueño
├── dto             Objetos de entrada y salida de la API
├── entity          Entidades JPA del modelo relacional
├── exception       Manejo global de errores con respuestas en JSON
├── repository      Repositorios Spring Data
├── security        JWT, filtro de autenticación y reglas por rol
├── service         Reglas de negocio
└── util            Traducción de errores de PostgreSQL a mensajes claros
```

Los errores de la base (llaves duplicadas, registros con dependientes, reglas de los triggers) llegan al cliente como mensajes en español con el código HTTP adecuado.

Las clases y los métodos públicos tienen Javadoc; se genera con `./mvnw javadoc:javadoc` en `target/site/apidocs`. Los diagramas de clases están en la carpeta de documentación (`13_Diagrama_Clases_Back.drawio`).

## Despliegue en la VM

1. Ejecutar los scripts 01 a 04 en el PostgreSQL de la VM y cambiar las contraseñas de `04_usuarios.sql`.
2. Copiar el WAR a la VM.
3. Definir las variables de entorno con el usuario `app_motos` y un `JWT_SECRET` propio.
4. Ejecutar `java -jar proyectoFinal-0.0.1-SNAPSHOT.war` y abrir el puerto 8080.

Las reservas activas vencidas se marcan solas cada noche; el dueño también puede hacerlo con «Marcar vencidas» en Administración > Reservas.

## Integrantes

- Mateo Robles Rodriguez
- Gabriel Reyes Ariza
- Samuel Romero
