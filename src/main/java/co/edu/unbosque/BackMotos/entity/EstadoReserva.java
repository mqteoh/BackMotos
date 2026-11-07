package co.edu.unbosque.BackMotos.entity;

/** Estados de una reserva. */
public enum EstadoReserva {
	/** Vigente: la moto está apartada. */
	ACTIVA,

	/** El cliente compró la moto. */
	CONCRETADA,

	/** Se canceló y la moto quedó libre. */
	CANCELADA,

	/** Pasó la fecha de vencimiento sin compra. */
	VENCIDA
}
