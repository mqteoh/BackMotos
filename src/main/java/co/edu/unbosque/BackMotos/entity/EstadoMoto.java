package co.edu.unbosque.BackMotos.entity;

/**
 * Estado de una moto del inventario. Lo cambian los triggers de la base al reservar, cancelar o vender.
 */
public enum EstadoMoto {
	/** Se puede reservar o vender. */
	DISPONIBLE,

	/** Un cliente la apartó; solo él la puede comprar. */
	RESERVADA,

	/** Ya tiene una venta registrada. */
	VENDIDA
}
