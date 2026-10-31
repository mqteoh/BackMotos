package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.util.List;

public record ComprasClienteDTO(List<VentaMotoDTO> motos, List<VentaAccesorioDTO> accesorios, BigDecimal totalGastado) {
}
