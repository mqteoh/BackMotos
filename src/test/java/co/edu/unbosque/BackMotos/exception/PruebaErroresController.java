package co.edu.unbosque.BackMotos.exception;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.MarcaDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.ModeloRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.service.MarcaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/prueba-errores")
class PruebaErroresController {

	private final ReservaRepository reservaRepository;
	private final MotoRepository motoRepository;
	private final ClienteRepository clienteRepository;
	private final ModeloRepository modeloRepository;
	private final SucursalRepository sucursalRepository;
	private final MarcaService marcaService;

	PruebaErroresController(ReservaRepository reservaRepository, MotoRepository motoRepository,
			ClienteRepository clienteRepository, ModeloRepository modeloRepository,
			SucursalRepository sucursalRepository, MarcaService marcaService) {
		this.reservaRepository = reservaRepository;
		this.motoRepository = motoRepository;
		this.clienteRepository = clienteRepository;
		this.modeloRepository = modeloRepository;
		this.sucursalRepository = sucursalRepository;
		this.marcaService = marcaService;
	}

	@PostMapping("/reserva-moto-vendida")
	void reservarMotoVendida() {
		Reserva reserva = new Reserva();
		reserva.setFechaReserva(LocalDateTime.of(2026, 10, 30, 9, 0));
		reserva.setFechaVencimiento(LocalDate.of(2026, 11, 6));
		reserva.setMoto(motoRepository.getReferenceById("JKL78D"));
		reserva.setCliente(clienteRepository.getReferenceById(2));
		reservaRepository.saveAndFlush(reserva);
	}

	@PostMapping("/moto-externa-con-sede")
	void motoExternaConSede() {
		Moto moto = new Moto();
		moto.setPlaca("ERR10Z");
		moto.setModelo(modeloRepository.getReferenceById(new ModeloId(1, 1)));
		moto.setSucursal(sucursalRepository.getReferenceById(1));
		moto.setOrigen(OrigenMoto.EXTERNA);
		moto.setAnio(2020);
		moto.setColor("Rojo");
		moto.setKilometraje(1000);
		moto.setPrecio(new BigDecimal("1000000"));
		moto.setFechaIngreso(LocalDate.of(2026, 10, 30));
		motoRepository.saveAndFlush(moto);
	}

	@PostMapping("/marca")
	MarcaDTO crearMarca(@Valid @RequestBody MarcaDTO dto) {
		return marcaService.crear(dto);
	}

	@GetMapping("/marca/{id}")
	MarcaDTO buscarMarca(@PathVariable Integer id) {
		return marcaService.buscarPorId(id);
	}

	@DeleteMapping("/marca/{id}")
	void eliminarMarca(@PathVariable Integer id) {
		marcaService.eliminar(id);
	}
}
