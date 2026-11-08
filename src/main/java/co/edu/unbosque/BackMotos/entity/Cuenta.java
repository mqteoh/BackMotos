package co.edu.unbosque.BackMotos.entity;

import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import co.edu.unbosque.BackMotos.security.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Cuenta para entrar a la página web. Está fuera del modelo entidad-relación:
 * guarda el correo, la contraseña cifrada y el rol, y apunta al cliente o al empleado dueño de la cuenta.
 */
@Entity
@Table(name = "cuenta")
public class Cuenta {

	/** Identificador de la cuenta. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cuenta")
	private Integer idCuenta;

	/** Correo con el que se inicia sesión; es único. */
	@Column(name = "correo", nullable = false, length = 120, unique = true)
	private String correo;

	/** Contraseña cifrada con BCrypt; nunca se guarda en texto plano. */
	@Column(name = "password_hash", nullable = false, length = 60)
	@JdbcTypeCode(SqlTypes.CHAR)
	private String passwordHash;

	/** Rol que decide qué partes de la página puede usar. */
	@Enumerated(EnumType.STRING)
	@Column(name = "rol", nullable = false, length = 10)
	private Rol rol;

	/** Cliente dueño de la cuenta; solo para el rol CLIENTE. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", unique = true)
	private Cliente cliente;

	/** Empleado dueño de la cuenta; para los roles VENDEDOR, TECNICO y DUENO. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empleado", unique = true)
	private Empleado empleado;

	/** Constructor vacío que necesita JPA. */
	public Cuenta() {
	}

	/**
	 * Devuelve el identificador de la cuenta.
	 *
	 * @return el identificador de la cuenta
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}

	/**
	 * Cambia el identificador de la cuenta.
	 *
	 * @param idCuenta el identificador de la cuenta
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * Devuelve el correo con el que se inicia sesión.
	 *
	 * @return el correo con el que se inicia sesión
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * Cambia el correo con el que se inicia sesión.
	 *
	 * @param correo el correo con el que se inicia sesión
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * Devuelve la contraseña cifrada con BCrypt.
	 *
	 * @return la contraseña cifrada con BCrypt
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Cambia la contraseña cifrada con BCrypt.
	 *
	 * @param passwordHash la contraseña cifrada con BCrypt
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Devuelve el rol del usuario.
	 *
	 * @return el rol del usuario
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Cambia el rol del usuario.
	 *
	 * @param rol el rol del usuario
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * Devuelve el cliente dueño de la cuenta; null si la cuenta es de un empleado.
	 *
	 * @return el cliente dueño de la cuenta; null si la cuenta es de un empleado
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Cambia el cliente dueño de la cuenta; null si la cuenta es de un empleado.
	 *
	 * @param cliente el cliente dueño de la cuenta; null si la cuenta es de un empleado
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Devuelve el empleado dueño de la cuenta; null si la cuenta es de un cliente.
	 *
	 * @return el empleado dueño de la cuenta; null si la cuenta es de un cliente
	 */
	public Empleado getEmpleado() {
		return empleado;
	}

	/**
	 * Cambia el empleado dueño de la cuenta; null si la cuenta es de un cliente.
	 *
	 * @param empleado el empleado dueño de la cuenta; null si la cuenta es de un cliente
	 */
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Cuenta otra)) {
			return false;
		}
		return idCuenta != null && idCuenta.equals(otra.idCuenta);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idCuenta);
	}
}
