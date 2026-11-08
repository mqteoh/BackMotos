package co.edu.unbosque.BackMotos.security;

/** Roles de las cuentas: cliente, vendedor, técnico y dueño. */
public enum Rol {
	/** Cliente con cuenta web. */
	CLIENTE,

	/** Dueño: ve y administra todo. */
	DUENO,

	/** Vendedor: clientes, reservas y ventas de su sede. */
	VENDEDOR,

	/** Técnico: reparaciones de su sede. */
	TECNICO
}
