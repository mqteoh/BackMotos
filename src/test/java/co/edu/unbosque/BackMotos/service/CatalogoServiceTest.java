package co.edu.unbosque.BackMotos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.MarcaDTO;
import co.edu.unbosque.BackMotos.dto.ModeloDTO;
import co.edu.unbosque.BackMotos.dto.ProveedorDTO;
import co.edu.unbosque.BackMotos.dto.SucursalDTO;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;

@SpringBootTest
@Transactional
class CatalogoServiceTest {

	@Autowired
	private MarcaService marcaService;

	@Autowired
	private ModeloService modeloService;

	@Autowired
	private SucursalService sucursalService;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private AccesorioService accesorioService;

	@Test
	void marcasSeListanOrdenadasYSinExponerEntidades() {
		assertThat(marcaService.listar()).hasSize(8).first().extracting(MarcaDTO::getNombre).isEqualTo("AKT");
		assertThat(marcaService.contar()).isEqualTo(8);
		assertThat(marcaService.existe(1)).isTrue();
	}

	@Test
	void creaYActualizaUnaMarca() {
		MarcaDTO creada = marcaService.crear(new MarcaDTO("Royal Enfield", "India"));
		Integer id = creada.getIdMarca();
		assertThat(id).isNotNull();
		creada.setPaisOrigen("Reino Unido");
		creada.setIdMarca(12345);
		MarcaDTO actualizada = marcaService.actualizar(id, creada);
		assertThat(actualizada.getPaisOrigen()).isEqualTo("Reino Unido");
		assertThat(actualizada.getIdMarca()).isEqualTo(id);
	}

	@Test
	void rechazaMarcaDuplicadaEInexistente() {
		assertThatThrownBy(() -> marcaService.crear(new MarcaDTO("HONDA", "Japón")))
				.isInstanceOf(ConflictException.class);
		assertThatThrownBy(() -> marcaService.buscarPorId(999)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void modeloNuevoRecibeElSiguienteNumeroDeSuMarca() {
		ModeloDTO dto = new ModeloDTO();
		dto.setIdMarca(1);
		dto.setNombre("R15 V4");
		dto.setCilindraje(155);
		dto.setTipoMoto("DEPORTIVA");
		dto.setPotenciaHp(new BigDecimal("18.4"));
		dto.setCapacidadTanque(new BigDecimal("11.0"));
		ModeloDTO creado = modeloService.crear(dto);
		assertThat(creado.getNroModelo()).isEqualTo(5);
		assertThat(creado.getNombreMarca()).isEqualTo("Yamaha");
		assertThat(modeloService.listarPorMarca(1)).hasSize(5);
		assertThat(modeloService.buscarPorId(new ModeloId(1, 5)).getNombre()).isEqualTo("R15 V4");
	}

	@Test
	void modeloNoPuedeCambiarDeMarcaNiRepetirNombre() {
		ModeloDTO mt03 = modeloService.buscarPorId(new ModeloId(1, 3));
		mt03.setIdMarca(2);
		assertThatThrownBy(() -> modeloService.actualizar(new ModeloId(1, 3), mt03))
				.isInstanceOf(BadRequestException.class);
		mt03.setIdMarca(1);
		mt03.setNombre("FZ 2.0");
		assertThatThrownBy(() -> modeloService.actualizar(new ModeloId(1, 3), mt03))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void sedeYProveedorValidanSusUnicos() {
		SucursalDTO sede = sucursalService.buscarPorId(1);
		SucursalDTO copia = new SucursalDTO();
		copia.setNombre(sede.getNombre());
		copia.setCiudad("Bogotá");
		copia.setBarrio("Centro");
		copia.setCalle("Calle 1");
		copia.setNumeroDireccion("1-1");
		assertThatThrownBy(() -> sucursalService.crear(copia)).isInstanceOf(ConflictException.class);
		ProveedorDTO proveedor = proveedorService.buscarPorId("900456789-1");
		assertThatThrownBy(() -> proveedorService.crear(proveedor)).isInstanceOf(ConflictException.class);
	}

	@Test
	void accesorioSeMapeaCompleto() {
		assertThat(accesorioService.buscarPorId(6)).satisfies(a -> {
			assertThat(a.getNombre()).isEqualTo("Intercomunicador Sena 5S");
			assertThat(a.getMarca()).isEqualTo("Sena");
			assertThat(a.getPrecio()).isEqualByComparingTo("699000");
		});
	}
}
