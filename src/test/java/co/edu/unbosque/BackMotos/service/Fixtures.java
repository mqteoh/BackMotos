package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import co.edu.unbosque.BackMotos.configuration.RelojConfig;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.Marca;
import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

final class Fixtures {

	static final Clock RELOJ = Clock.fixed(ZonedDateTime.of(2026, 10, 31, 10, 0, 0, 0, RelojConfig.ZONA).toInstant(),
			RelojConfig.ZONA);

	private Fixtures() {
	}

	static Sucursal sede(int id, String nombre) {
		Sucursal s = new Sucursal();
		s.setIdSucursal(id);
		s.setNombre(nombre);
		s.setCiudad("Bogotá");
		return s;
	}

	static Moto moto(String placa, Sucursal sede) {
		Marca marca = new Marca("Yamaha", "Japón");
		marca.setIdMarca(1);
		Modelo modelo = new Modelo();
		modelo.setId(new ModeloId(1, 2));
		modelo.setMarca(marca);
		modelo.setNombre("NMAX 155");
		modelo.setTipoMoto("SCOOTER");
		Moto moto = new Moto();
		moto.setPlaca(placa);
		moto.setModelo(modelo);
		moto.setSucursal(sede);
		moto.setAnio(2026);
		moto.setColor("Gris");
		moto.setKilometraje(0);
		moto.setPrecio(new BigDecimal("15900000"));
		moto.setFechaIngreso(LocalDate.of(2026, 1, 10));
		return moto;
	}

	static Cliente cliente(int id) {
		Cliente c = new Cliente();
		c.setIdCliente(id);
		c.setNombres("Cliente");
		c.setApellidos("Prueba " + id);
		return c;
	}

	static Vendedor vendedor(int id, Sucursal sede) {
		Vendedor v = new Vendedor();
		v.setIdEmpleado(id);
		v.setNombres("Vendedor");
		v.setApellidos("Prueba");
		v.setSucursal(sede);
		return v;
	}

	static Tecnico tecnico(int id, Sucursal sede) {
		Tecnico t = new Tecnico();
		t.setIdEmpleado(id);
		t.setNombres("Técnico");
		t.setApellidos("Prueba");
		t.setSucursal(sede);
		return t;
	}

	static UsuarioAutenticado usuario(int id, Rol rol, Integer sede) {
		return new UsuarioAutenticado(id, "u" + id + "@x.test", null, "Usuario " + id, rol, sede);
	}
}
