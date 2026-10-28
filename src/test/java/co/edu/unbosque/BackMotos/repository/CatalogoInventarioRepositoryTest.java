package co.edu.unbosque.BackMotos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.MotoDisponible;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CatalogoInventarioRepositoryTest {

	@Autowired
	private MarcaRepository marcaRepository;

	@Autowired
	private ModeloRepository modeloRepository;

	@Autowired
	private SucursalRepository sucursalRepository;

	@Autowired
	private MotoRepository motoRepository;

	@Autowired
	private MotoDisponibleRepository motoDisponibleRepository;

	@Test
	void cargaMarcasYSucursalesDeLosDatosDePrueba() {
		assertThat(marcaRepository.count()).isEqualTo(8);
		assertThat(marcaRepository.findByNombreIgnoreCase("yamaha")).get()
				.extracting(m -> m.getPaisOrigen()).isEqualTo("Japón");
		assertThat(sucursalRepository.count()).isEqualTo(4);
		assertThat(sucursalRepository.findByCiudadIgnoreCaseOrderByNombre("Bogotá")).hasSize(2);
	}

	@Test
	void modeloSeIdentificaPorMarcaYNumeroDentroDeLaMarca() {
		Modelo mt03 = modeloRepository.findById(new ModeloId(1, 3)).orElseThrow();
		assertThat(mt03.getNombre()).isEqualTo("MT-03");
		assertThat(mt03.getMarca().getNombre()).isEqualTo("Yamaha");
		assertThat(mt03.getPotenciaHp()).isEqualByComparingTo("41.4");
		assertThat(modeloRepository.findByIdIdMarcaOrderByIdNroModelo(1)).hasSize(4);
		assertThat(modeloRepository.siguienteNroModelo(1)).isEqualTo(5);
		assertThat(modeloRepository.existsByIdIdMarcaAndNombreIgnoreCase(2, "cb 190r")).isTrue();
	}

	@Test
	void motoDeInventarioTraeModeloYSucursal() {
		Moto moto = motoRepository.findById("GTK21F").orElseThrow();
		assertThat(moto.getOrigen()).isEqualTo(OrigenMoto.INVENTARIO);
		assertThat(moto.getModelo().getNombre()).isEqualTo("FZ 2.0");
		assertThat(moto.getSucursal().getNombre()).isEqualTo("Sede Chapinero");
		assertThat(moto.getPrecio()).isNotNull();
	}

	@Test
	void motoExternaNoTieneSucursalNiPrecio() {
		Moto moto = motoRepository.findById("BCD47E").orElseThrow();
		assertThat(moto.getOrigen()).isEqualTo(OrigenMoto.EXTERNA);
		assertThat(moto.getSucursal()).isNull();
		assertThat(moto.getPrecio()).isNull();
		assertThat(motoRepository.findByOrigen(OrigenMoto.EXTERNA)).hasSize(6);
	}

	@Test
	void guardaUnaMotoNuevaDeInventario() {
		Moto nueva = motoInventario("PRB10Z");
		motoRepository.saveAndFlush(nueva);
		assertThat(motoRepository.findById("PRB10Z")).isPresent();
		assertThat(motoRepository.findBySucursalIdSucursal(1)).extracting(Moto::getPlaca).contains("PRB10Z");
	}

	@Test
	void laBaseRechazaUnaMotoExternaConSucursal() {
		Moto invalida = motoInventario("PRB11Z");
		invalida.setOrigen(OrigenMoto.EXTERNA);
		assertThatThrownBy(() -> motoRepository.saveAndFlush(invalida)).rootCause()
				.isInstanceOf(SQLException.class).hasMessageContaining("ck_moto_origen_datos")
				.extracting(e -> ((SQLException) e).getErrorCode()).isEqualTo(3819);
	}

	@Test
	void vistaDeDisponiblesExcluyeVendidasYReservadas() {
		assertThat(motoDisponibleRepository.count()).isEqualTo(12);
		assertThat(motoDisponibleRepository.findAll()).extracting(MotoDisponible::getPlaca)
				.doesNotContain("JKL78D", "LMN24P", "BCD47E").contains("GTK22F");
	}

	private Moto motoInventario(String placa) {
		Moto moto = new Moto();
		moto.setPlaca(placa);
		moto.setModelo(modeloRepository.getReferenceById(new ModeloId(1, 1)));
		moto.setSucursal(sucursalRepository.getReferenceById(1));
		moto.setOrigen(OrigenMoto.INVENTARIO);
		moto.setAnio(2026);
		moto.setColor("Gris");
		moto.setKilometraje(0);
		moto.setPrecio(new BigDecimal("10900000"));
		moto.setFechaIngreso(LocalDate.of(2026, 10, 28));
		return moto;
	}
}
