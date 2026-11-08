package co.edu.unbosque.BackMotos.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Reserva de una moto por un cliente (tabla reserva, fuera del modelo). */
@Entity
@Table(name = "reserva")
public class Reserva {

	/** Identificador de la reserva. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reserva")
	private Integer idReserva;

	/** Fecha en que se hizo la reserva. */
	@Column(name = "fecha_reserva", nullable = false)
	private LocalDateTime fechaReserva;

	/** Fecha en que vence la reserva. */
	@Column(name = "fecha_vencimiento", nullable = false)
	private LocalDate fechaVencimiento;

	/** Estado de la reserva: ACTIVA, CONCRETADA, CANCELADA o VENCIDA. */
	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 12)
	private EstadoReserva estado = EstadoReserva.ACTIVA;

	/** Moto reservada. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "placa")
	private Moto moto;

	/** Cliente que hizo la reserva. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	/** Constructor vacío que necesita JPA. */
	public Reserva() {
	}

	/**
	 * Devuelve el identificador de la reserva.
	 *
	 * @return el identificador de la reserva
	 */
	public Integer getIdReserva() {
		return idReserva;
	}

	/**
	 * Cambia el identificador de la reserva.
	 *
	 * @param idReserva el identificador de la reserva
	 */
	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	/**
	 * Devuelve la fecha en que se hizo la reserva.
	 *
	 * @return la fecha en que se hizo la reserva
	 */
	public LocalDateTime getFechaReserva() {
		return fechaReserva;
	}

	/**
	 * Cambia la fecha en que se hizo la reserva.
	 *
	 * @param fechaReserva la fecha en que se hizo la reserva
	 */
	public void setFechaReserva(LocalDateTime fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	/**
	 * Devuelve la fecha en que vence la reserva.
	 *
	 * @return la fecha en que vence la reserva
	 */
	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	/**
	 * Cambia la fecha en que vence la reserva.
	 *
	 * @param fechaVencimiento la fecha en que vence la reserva
	 */
	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * Devuelve el estado de la reserva: ACTIVA, CONCRETADA, CANCELADA o VENCIDA.
	 *
	 * @return el estado de la reserva: ACTIVA, CONCRETADA, CANCELADA o VENCIDA
	 */
	public EstadoReserva getEstado() {
		return estado;
	}

	/**
	 * Cambia el estado de la reserva: ACTIVA, CONCRETADA, CANCELADA o VENCIDA.
	 *
	 * @param estado el estado de la reserva: ACTIVA, CONCRETADA, CANCELADA o VENCIDA
	 */
	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	/**
	 * Devuelve la moto reservada.
	 *
	 * @return la moto reservada
	 */
	public Moto getMoto() {
		return moto;
	}

	/**
	 * Cambia la moto reservada.
	 *
	 * @param moto la moto reservada
	 */
	public void setMoto(Moto moto) {
		this.moto = moto;
	}

	/**
	 * Devuelve el cliente que hizo la reserva.
	 *
	 * @return el cliente que hizo la reserva
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Cambia el cliente que hizo la reserva.
	 *
	 * @param cliente el cliente que hizo la reserva
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Reserva otra)) {
			return false;
		}
		return idReserva != null && idReserva.equals(otra.idReserva);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idReserva);
	}
}
