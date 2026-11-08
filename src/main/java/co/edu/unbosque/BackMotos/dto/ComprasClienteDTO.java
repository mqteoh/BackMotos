package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Compras de un cliente: motos, accesorios y total gastado.
 *
 * @param motos compras de motos del cliente
 * @param accesorios compras de accesorios del cliente
 * @param totalGastado suma de todas las compras
 */
public record ComprasClienteDTO(List<VentaMotoDTO> motos, List<VentaAccesorioDTO> accesorios, BigDecimal totalGastado) {
}
