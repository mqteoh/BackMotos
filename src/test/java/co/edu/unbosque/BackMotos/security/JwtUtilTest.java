package co.edu.unbosque.BackMotos.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

	private static final String SECRETO = "secreto-de-prueba-con-mas-de-treinta-y-dos-caracteres";

	private final UsuarioAutenticado vendedora = new UsuarioAutenticado(2, "laura.gomez@concesionario-motos.test",
			"hash", "Laura Gómez Torres", Rol.VENDEDOR, 1);

	@Test
	void elTokenConservaLosDatosDelUsuario() {
		JwtUtil jwt = new JwtUtil(SECRETO, 60_000);
		UsuarioAutenticado leido = jwt.validar(jwt.generarToken(vendedora)).orElseThrow();
		assertThat(leido.getId()).isEqualTo(2);
		assertThat(leido.getCorreo()).isEqualTo("laura.gomez@concesionario-motos.test");
		assertThat(leido.getRol()).isEqualTo(Rol.VENDEDOR);
		assertThat(leido.getIdSucursal()).isEqualTo(1);
		assertThat(leido.getNombre()).isEqualTo("Laura Gómez Torres");
		assertThat(leido.getPassword()).isNull();
	}

	@Test
	void usuarioSinSedeNoLlevaSucursal() {
		JwtUtil jwt = new JwtUtil(SECRETO, 60_000);
		UsuarioAutenticado dueno = new UsuarioAutenticado(1, "dueno@x.test", "h", "Dueño", Rol.DUENO, null);
		assertThat(jwt.validar(jwt.generarToken(dueno)).orElseThrow().getIdSucursal()).isNull();
	}

	@Test
	void tokenAlteradoVencidoOFirmadoConOtraClaveNoEsValido() {
		JwtUtil jwt = new JwtUtil(SECRETO, 60_000);
		String token = jwt.generarToken(vendedora);
		String alterado = token.substring(0, token.length() - 3) + (token.endsWith("aaa") ? "bbb" : "aaa");
		assertThat(jwt.validar(alterado)).isEmpty();
		assertThat(jwt.validar(new JwtUtil(SECRETO, -1_000).generarToken(vendedora))).isEmpty();
		assertThat(new JwtUtil(SECRETO + "-otro", 60_000).validar(token)).isEmpty();
		assertThat(jwt.validar("no-es-un-token")).isEmpty();
	}

	@Test
	void rechazaSecretoCorto() {
		assertThatThrownBy(() -> new JwtUtil("corto", 60_000)).isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("32");
	}
}
