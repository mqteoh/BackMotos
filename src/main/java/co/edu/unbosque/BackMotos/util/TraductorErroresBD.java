package co.edu.unbosque.BackMotos.util;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.http.HttpStatus;

/**
 * Traduce los errores de PostgreSQL a mensajes para el usuario.
 * Usa el código SQLState y el nombre de la restricción que falló.
 */
public final class TraductorErroresBD {

	/**
	 * Estado HTTP y mensaje para responder.
	 *
	 * @param estado código de estado HTTP que se responde
	 * @param mensaje mensaje en palabras simples para el usuario
	 */
	public record ErrorBD(HttpStatus estado, String mensaje) {
	}

	/** Código SQLSTATE que usan los triggers para las reglas del negocio. */
	private static final String SQLSTATE_REGLA_NEGOCIO = "45000";
	/** Código SQLSTATE de un valor repetido. */
	private static final String DUPLICADO = "23505";
	/** Código SQLSTATE de un dato obligatorio vacío. */
	private static final String NULO_NO_PERMITIDO = "23502";
	/** Código SQLSTATE de una llave foránea que no se cumple. */
	private static final String LLAVE_FORANEA = "23503";
	/** Código SQLSTATE de una restricción CHECK que no se cumple. */
	private static final String CHECK_VIOLADO = "23514";

	/** Reconoce el error de borrar un registro del que dependen otros. */
	private static final Pattern CON_DEPENDIENTES = Pattern.compile("still referenced|todav\\S+ es referida",
			Pattern.CASE_INSENSITIVE);

	/** Mensaje para una cantidad no válida. */
	private static final String CANTIDAD = "La cantidad debe ser mayor que cero.";
	/** Mensaje para un precio no válido. */
	private static final String PRECIO = "El precio debe ser mayor que cero.";
	/** Mensaje para un precio unitario no válido. */
	private static final String PRECIO_UNITARIO = "El precio unitario debe ser mayor que cero.";
	/** Mensaje para un teléfono no válido. */
	private static final String TELEFONO = "El teléfono debe tener entre 7 y 15 dígitos.";
	/** Mensaje para un correo no válido. */
	private static final String CORREO = "El correo no es válido.";
	/** Mensaje para un método de pago no válido. */
	private static final String PAGO = "El método de pago no es válido.";
	/** Mensaje para un tipo de factura no válido. */
	private static final String FACTURA = "El tipo de factura no es válido.";

	/** Mensaje para cada restricción de la base, según su nombre. */
	private static final Map<String, String> REGLAS = Map.ofEntries(
			Map.entry("ck_modelo_id", "El número de modelo debe ser mayor que cero."),
			Map.entry("ck_modelo_cilindraje", "El cilindraje debe estar entre 50 y 2500 cc."),
			Map.entry("ck_modelo_tipo", "El tipo de moto no es válido."),
			Map.entry("ck_modelo_potencia", "La potencia debe ser mayor que cero."),
			Map.entry("ck_modelo_tanque", "La capacidad del tanque debe ser mayor que cero."),
			Map.entry("ck_moto_placa", "La placa debe tener el formato AAA00A."),
			Map.entry("ck_moto_anio", "El año de la moto debe estar entre 1980 y 2030."),
			Map.entry("ck_moto_precio", PRECIO),
			Map.entry("ck_moto_estado", "El estado de la moto debe ser DISPONIBLE, RESERVADA o VENDIDA."),
			Map.entry("ck_moto_kilometraje", "El kilometraje no puede ser negativo."),
			Map.entry("ck_telefono_cliente_numero", TELEFONO),
			Map.entry("ck_correo_cliente_formato", CORREO),
			Map.entry("ck_empleado_salario", "El salario debe ser mayor que cero."),
			Map.entry("ck_empleado_jefe", "Un empleado no puede ser su propio jefe."),
			Map.entry("ck_telefono_empleado_numero", TELEFONO),
			Map.entry("ck_cuenta_correo", CORREO),
			Map.entry("ck_cuenta_rol", "El rol debe ser CLIENTE, VENDEDOR, TECNICO o DUENO."),
			Map.entry("ck_cuenta_titular", "La cuenta de un cliente va unida al cliente y la de un empleado al empleado."),
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
			Map.entry("ck_venta_accesorio_valor", "El valor de la venta no puede ser negativo."),
			Map.entry("ck_repuesto_precio", PRECIO),
			Map.entry("ck_reparacion_costo", "El costo no puede ser negativo."),
			Map.entry("ck_reparacion_repuestos_cantidad", CANTIDAD),
			Map.entry("ck_reparacion_repuestos_precio", PRECIO_UNITARIO),
			Map.entry("ck_proveedor_nit", "El NIT debe tener el formato 000000000-0."),
			Map.entry("ck_suministros_cantidad", CANTIDAD),
			Map.entry("ck_suministros_costo", "El costo unitario debe ser mayor que cero."),
			Map.entry("uq_marca_nombre", "Ya existe una marca con ese nombre."),
			Map.entry("uq_modelo_marca_nombre", "La marca ya tiene un modelo con ese nombre."),
			Map.entry("uq_sucursal_nombre", "Ya existe una sede con ese nombre."),
			Map.entry("uq_cliente_documento", "Ya existe un cliente con ese documento."),
			Map.entry("pk_correo_cliente", "Ese correo ya está registrado para otro cliente."),
			Map.entry("uq_empleado_documento", "Ya existe un empleado con ese documento."),
			Map.entry("uq_cuenta_correo", "Ese correo de acceso ya está registrado."),
			Map.entry("uq_cuenta_cliente", "Ese cliente ya tiene una cuenta."),
			Map.entry("uq_cuenta_empleado", "Ese empleado ya tiene una cuenta."),
			Map.entry("pk_venta_moto", "Esta moto ya fue vendida."),
			Map.entry("pk_suministros", "Ya existe una entrega de ese proveedor, repuesto y sede en esa fecha."),
			Map.entry("uq_proveedor_nombre", "Ya existe un proveedor con ese nombre."));

	/** No se crean objetos de esta clase. */
	private TraductorErroresBD() {
	}

	/**
	 * Busca en la cadena de causas un error de PostgreSQL conocido.
	 *
	 * @param error excepción lanzada al guardar
	 * @return estado y mensaje, o vacío si no es un error conocido
	 */
	public static Optional<ErrorBD> traducir(Throwable error) {
		SQLException sql = buscarSQLException(error);
		if (sql == null) {
			return Optional.empty();
		}
		ServerErrorMessage servidor = sql instanceof PSQLException psql ? psql.getServerErrorMessage() : null;
		String mensaje = servidor != null && servidor.getMessage() != null ? servidor.getMessage()
				: sql.getMessage() == null ? "" : sql.getMessage();
		String restriccion = servidor == null ? null : servidor.getConstraint();
		String estado = sql.getSQLState() == null ? "" : sql.getSQLState();
		return switch (estado) {
		case SQLSTATE_REGLA_NEGOCIO -> Optional.of(new ErrorBD(HttpStatus.CONFLICT, mensaje));
		case CHECK_VIOLADO -> Optional.of(new ErrorBD(HttpStatus.CONFLICT,
				porNombre(restriccion, "La operación no cumple una regla de la base de datos.")));
		case DUPLICADO -> Optional.of(new ErrorBD(HttpStatus.CONFLICT,
				porNombre(restriccion, "Ya existe un registro con esa clave.")));
		case LLAVE_FORANEA -> Optional.of(conDependientes(servidor, sql)
				? new ErrorBD(HttpStatus.CONFLICT, "No se puede eliminar porque otros registros dependen de él.")
				: new ErrorBD(HttpStatus.BAD_REQUEST, "Uno de los registros relacionados no existe."));
		case NULO_NO_PERMITIDO -> Optional.of(new ErrorBD(HttpStatus.BAD_REQUEST, "Falta un dato obligatorio."));
		default -> Optional.empty();
		};
	}

	/**
	 * Busca el mensaje de una restricción por su nombre.
	 *
	 * @param restriccion nombre de la restricción; puede ser null
	 * @param porDefecto mensaje si no se conoce
	 * @return el mensaje para el usuario
	 */
	private static String porNombre(String restriccion, String porDefecto) {
		return restriccion == null ? porDefecto : REGLAS.getOrDefault(restriccion, porDefecto);
	}

	/**
	 * Dice si el error es por borrar un registro del que dependen otros.
	 *
	 * @param servidor detalle del error de PostgreSQL; puede ser null
	 * @param sql error de JDBC
	 * @return true si otros registros dependen del que se quiso borrar
	 */
	private static boolean conDependientes(ServerErrorMessage servidor, SQLException sql) {
		String texto = (servidor == null ? "" : servidor.getDetail() + " " + servidor.getMessage()) + " "
				+ sql.getMessage();
		return CON_DEPENDIENTES.matcher(texto).find();
	}

	/**
	 * Busca la SQLException más interna dentro de la cadena de causas.
	 *
	 * @param error error que se lanzó
	 * @return la SQLException, o null si no hay
	 */
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
