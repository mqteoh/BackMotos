package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.MotoInventarioDTO;
import co.edu.unbosque.BackMotos.dto.NuevaVentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.NuevaVentaMotoDTO;
import co.edu.unbosque.BackMotos.dto.VentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.VentaMotoDTO;
import co.edu.unbosque.BackMotos.entity.Accesorio;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import co.edu.unbosque.BackMotos.entity.VentaAccesorio;
import co.edu.unbosque.BackMotos.entity.VentaMoto;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.AccesorioRepository;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;
import co.edu.unbosque.BackMotos.repository.VendedorRepository;
import co.edu.unbosque.BackMotos.repository.VentaAccesorioRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import jakarta.persistence.EntityManager;

@Service
public class VentaService {

	private static final Sort RECIENTES = Sort.by(Sort.Direction.DESC, "fechaVenta");

	private final VentaMotoRepository ventaMotoRepository;
	private final VentaAccesorioRepository ventaAccesorioRepository;
	private final MotoRepository motoRepository;
	private final ClienteRepository clienteRepository;
	private final VendedorRepository vendedorRepository;
	private final ReservaRepository reservaRepository;
	private final AccesorioRepository accesorioRepository;
	private final EntityManager entityManager;
	private final Clock reloj;

	public VentaService(VentaMotoRepository ventaMotoRepository, VentaAccesorioRepository ventaAccesorioRepository,
			MotoRepository motoRepository, ClienteRepository clienteRepository, VendedorRepository vendedorRepository,
			ReservaRepository reservaRepository, AccesorioRepository accesorioRepository, EntityManager entityManager,
			Clock reloj) {
		this.ventaMotoRepository = ventaMotoRepository;
		this.ventaAccesorioRepository = ventaAccesorioRepository;
		this.motoRepository = motoRepository;
		this.clienteRepository = clienteRepository;
		this.vendedorRepository = vendedorRepository;
		this.reservaRepository = reservaRepository;
		this.accesorioRepository = accesorioRepository;
		this.entityManager = entityManager;
		this.reloj = reloj;
	}

	@Transactional
	public VentaMotoDTO registrarVentaMoto(UsuarioAutenticado usuario, NuevaVentaMotoDTO datos) {
		Vendedor vendedor = vendedor(usuario);
		String placa = datos.placa().trim().toUpperCase(Locale.ROOT);
		Moto moto = motoRepository.findById(placa)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la moto con placa " + placa + "."));
		if (moto.getOrigen() != OrigenMoto.INVENTARIO) {
			throw new ConflictException("Solo se venden motos del inventario.");
		}
		if (!moto.getSucursal().getIdSucursal().equals(vendedor.getSucursal().getIdSucursal())) {
			throw new ConflictException("La moto " + placa + " está en la " + moto.getSucursal().getNombre()
					+ "; solo puede venderla un vendedor de esa sede.");
		}
		if (ventaMotoRepository.existsByMotoPlaca(placa)) {
			throw new ConflictException("La moto " + placa + " ya fue vendida.");
		}
		Cliente cliente = cliente(datos.idCliente());
		Reserva reserva = reservaParaVenta(placa, cliente, datos.idReserva());
		VentaMoto venta = new VentaMoto();
		venta.setFechaVenta(ahora());
		venta.setMetodoPago(datos.metodoPago());
		venta.setTipoFactura(datos.tipoFactura());
		venta.setValorTotal(datos.valorTotal());
		venta.setMoto(moto);
		venta.setCliente(cliente);
		venta.setVendedor(vendedor);
		venta.setReserva(reserva);
		ventaMotoRepository.saveAndFlush(venta);
		if (reserva != null) {
			entityManager.refresh(reserva);
		}
		return VentaMotoDTO.de(venta);
	}

	@Transactional
	public VentaAccesorioDTO registrarVentaAccesorio(UsuarioAutenticado usuario, NuevaVentaAccesorioDTO datos) {
		Vendedor vendedor = vendedor(usuario);
		Cliente cliente = cliente(datos.idCliente());
		Set<Integer> vistos = new HashSet<>();
		for (NuevaVentaAccesorioDTO.Item item : datos.items()) {
			if (!vistos.add(item.idAccesorio())) {
				throw new BadRequestException("Cada accesorio debe aparecer una sola vez; ajuste la cantidad.");
			}
		}
		Map<Integer, Accesorio> accesorios = accesorioRepository.findAllById(vistos).stream()
				.collect(Collectors.toMap(Accesorio::getIdAccesorio, Function.identity()));
		VentaAccesorio venta = new VentaAccesorio();
		venta.setFechaVenta(ahora());
		venta.setMetodoPago(datos.metodoPago());
		venta.setTipoFactura(datos.tipoFactura());
		venta.setCliente(cliente);
		venta.setVendedor(vendedor);
		for (NuevaVentaAccesorioDTO.Item item : datos.items()) {
			Accesorio accesorio = accesorios.get(item.idAccesorio());
			if (accesorio == null) {
				throw new ResourceNotFoundException("No existe el accesorio con id " + item.idAccesorio() + ".");
			}
			BigDecimal precio = item.precioUnitario() == null ? accesorio.getPrecio() : item.precioUnitario();
			venta.agregarDetalle(accesorio, item.cantidad(), precio);
		}
		return VentaAccesorioDTO.de(ventaAccesorioRepository.saveAndFlush(venta));
	}

	@Transactional(readOnly = true)
	public List<VentaMotoDTO> ventasMoto(UsuarioAutenticado usuario) {
		List<VentaMoto> ventas = usuario.getRol() == Rol.DUENO ? ventaMotoRepository.findAll(RECIENTES)
				: ventaMotoRepository.findByVendedorIdEmpleadoOrderByFechaVentaDesc(usuario.getId());
		return ventas.stream().map(VentaMotoDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public List<VentaAccesorioDTO> ventasAccesorio(UsuarioAutenticado usuario) {
		List<VentaAccesorio> ventas = usuario.getRol() == Rol.DUENO ? ventaAccesorioRepository.findAll(RECIENTES)
				: ventaAccesorioRepository.findByVendedorIdEmpleadoOrderByFechaVentaDesc(usuario.getId());
		return ventas.stream().map(VentaAccesorioDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public List<MotoInventarioDTO> inventario(UsuarioAutenticado usuario, Integer idSucursal) {
		Integer sede = usuario.getRol() == Rol.DUENO ? idSucursal : usuario.getIdSucursal();
		Map<String, Reserva> reservas = reservaRepository.findByEstadoOrderByFechaVencimiento(EstadoReserva.ACTIVA)
				.stream().collect(Collectors.toMap(r -> r.getMoto().getPlaca(), Function.identity()));
		return motoRepository.sinVender(OrigenMoto.INVENTARIO, sede).stream().map(m -> {
			Reserva r = reservas.get(m.getPlaca());
			return new MotoInventarioDTO(m.getPlaca(), m.getModelo().getMarca().getNombre(), m.getModelo().getNombre(),
					m.getModelo().getTipoMoto(), m.getAnio(), m.getColor(), m.getPrecio(),
					m.getSucursal().getIdSucursal(), m.getSucursal().getNombre(), r != null,
					r == null ? null : r.getIdReserva(), r == null ? null : r.getCliente().getIdCliente(),
					r == null ? null : r.getCliente().getNombreCompleto(), r == null ? null : r.getFechaVencimiento());
		}).toList();
	}

	private Reserva reservaParaVenta(String placa, Cliente cliente, Integer idReserva) {
		if (idReserva != null) {
			Reserva reserva = reservaRepository.findById(idReserva)
					.orElseThrow(() -> new ResourceNotFoundException("No existe la reserva con id " + idReserva + "."));
			if (reserva.getEstado() != EstadoReserva.ACTIVA || !reserva.getMoto().getPlaca().equals(placa)
					|| !reserva.getCliente().getIdCliente().equals(cliente.getIdCliente())) {
				throw new ConflictException("La reserva " + idReserva + " no está activa o no corresponde a esta moto y cliente.");
			}
			return reserva;
		}
		return reservaRepository.findByMotoPlacaAndEstado(placa, EstadoReserva.ACTIVA).map(activa -> {
			if (!activa.getCliente().getIdCliente().equals(cliente.getIdCliente())) {
				throw new ConflictException("La moto " + placa + " está reservada por otro cliente hasta el "
						+ activa.getFechaVencimiento() + ".");
			}
			return activa;
		}).orElse(null);
	}

	private Vendedor vendedor(UsuarioAutenticado usuario) {
		return vendedorRepository.findById(usuario.getId())
				.orElseThrow(() -> new ConflictException("Solo un vendedor puede registrar ventas."));
	}

	private Cliente cliente(Integer idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con id " + idCliente + "."));
	}

	private LocalDateTime ahora() {
		return LocalDateTime.now(reloj).truncatedTo(ChronoUnit.SECONDS);
	}
}
