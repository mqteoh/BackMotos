package co.edu.unbosque.BackMotos.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.EmpleadoAdminDTO;
import co.edu.unbosque.BackMotos.entity.Cuenta;
import co.edu.unbosque.BackMotos.entity.Empleado;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.CuentaRepository;
import co.edu.unbosque.BackMotos.repository.EmpleadoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;
import co.edu.unbosque.BackMotos.security.Rol;

/**
 * Administración de empleados por parte del dueño. Cada empleado se guarda junto con su cuenta de acceso,
 * y el rol de la cuenta sale del tipo de empleado.
 */
@Service
public class EmpleadoAdminService {

	/** Acceso a la base para los empleados. */
	private final EmpleadoRepository empleadoRepository;
	/** Acceso a la base para las cuentas. */
	private final CuentaRepository cuentaRepository;
	/** Acceso a la base para las sedes. */
	private final SucursalRepository sucursalRepository;
	/** Cifra y compara contraseñas. */
	private final PasswordEncoder passwordEncoder;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param empleadoRepository acceso a la base para los empleados
	 * @param cuentaRepository acceso a la base para las cuentas
	 * @param sucursalRepository acceso a la base para las sedes
	 * @param passwordEncoder cifra y compara contraseñas
	 */
	public EmpleadoAdminService(EmpleadoRepository empleadoRepository, CuentaRepository cuentaRepository,
			SucursalRepository sucursalRepository, PasswordEncoder passwordEncoder) {
		this.empleadoRepository = empleadoRepository;
		this.cuentaRepository = cuentaRepository;
		this.sucursalRepository = sucursalRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Lista los empleados, con filtros opcionales.
	 *
	 * @param idSucursal sede, o null para todas
	 * @param tipo       tipo de empleado, o null para todos
	 * @return empleados ordenados por apellido
	 */
	@Transactional(readOnly = true)
	public List<EmpleadoAdminDTO> listar(Integer idSucursal, TipoEmpleado tipo) {
		Map<Integer, String> correos = cuentaRepository.deEmpleados().stream()
				.collect(Collectors.toMap(c -> c.getEmpleado().getIdEmpleado(), Cuenta::getCorreo));
		return empleadoRepository.findAll().stream()
				.filter(e -> idSucursal == null || e.getSucursal().getIdSucursal().equals(idSucursal))
				.filter(e -> tipo == null || e.getTipoEmpleado() == tipo)
				.sorted(Comparator.comparing(Empleado::getApellidos).thenComparing(Empleado::getNombres))
				.map(e -> EmpleadoAdminDTO.de(e, correos.get(e.getIdEmpleado()))).toList();
	}

	/**
	 * Busca un empleado con el correo de su cuenta.
	 *
	 * @param id identificador del empleado
	 * @return el empleado con el correo de su cuenta
	 */
	@Transactional(readOnly = true)
	public EmpleadoAdminDTO buscar(Integer id) {
		return aDTO(obtener(id));
	}

	/**
	 * Registra un empleado nuevo con su cuenta. La contraseña inicial es obligatoria.
	 *
	 * @param datos datos del empleado y de su cuenta
	 * @return el empleado guardado
	 */
	@Transactional
	public EmpleadoAdminDTO crear(EmpleadoAdminDTO datos) {
		if (datos.contrasena() == null) {
			throw new BadRequestException("La contraseña inicial es obligatoria.");
		}
		if (empleadoRepository.existsByDocumento(datos.documento())) {
			throw new ConflictException("Ya existe un empleado con el documento " + datos.documento() + ".");
		}
		validarCorreo(datos.correoAcceso());
		Empleado empleado = switch (datos.tipoEmpleado()) {
		case VENDEDOR -> new Vendedor();
		case TECNICO -> new Tecnico();
		case DUENO -> new Empleado();
		};
		empleado.setDocumento(datos.documento());
		copiar(datos, empleado);
		empleado = empleadoRepository.saveAndFlush(empleado);
		Cuenta cuenta = new Cuenta();
		cuenta.setEmpleado(empleado);
		cuenta.setRol(Rol.valueOf(datos.tipoEmpleado().name()));
		guardarCuenta(cuenta, datos);
		return aDTO(empleado);
	}

	/**
	 * Actualiza un empleado y su cuenta. El tipo de empleado no se puede cambiar.
	 *
	 * @param id    id del empleado
	 * @param datos datos nuevos; la contraseña solo cambia si viene
	 * @return el empleado actualizado
	 */
	@Transactional
	public EmpleadoAdminDTO actualizar(Integer id, EmpleadoAdminDTO datos) {
		Empleado empleado = obtener(id);
		if (empleado.getTipoEmpleado() != datos.tipoEmpleado()) {
			throw new ConflictException("No se puede cambiar el tipo de empleado; registre un empleado nuevo.");
		}
		if (!empleado.getDocumento().equals(datos.documento())) {
			if (empleadoRepository.existsByDocumento(datos.documento())) {
				throw new ConflictException("Ya existe un empleado con el documento " + datos.documento() + ".");
			}
			empleado.setDocumento(datos.documento());
		}
		if (datos.idJefe() != null && datos.idJefe().equals(id)) {
			throw new BadRequestException("Un empleado no puede ser su propio jefe.");
		}
		Cuenta cuenta = cuentaRepository.findByEmpleadoIdEmpleado(id).orElseGet(() -> {
			Cuenta nueva = new Cuenta();
			nueva.setEmpleado(empleado);
			nueva.setRol(Rol.valueOf(datos.tipoEmpleado().name()));
			return nueva;
		});
		if (cuenta.getCorreo() == null || !cuenta.getCorreo().equalsIgnoreCase(datos.correoAcceso().trim())) {
			validarCorreo(datos.correoAcceso());
		}
		if (cuenta.getPasswordHash() == null && datos.contrasena() == null) {
			throw new BadRequestException("El empleado no tiene cuenta; escriba una contraseña para crearla.");
		}
		copiar(datos, empleado);
		empleadoRepository.saveAndFlush(empleado);
		guardarCuenta(cuenta, datos);
		return aDTO(empleado);
	}

	/**
	 * Borra un empleado; su cuenta se borra con él. Nadie puede borrarse a sí mismo.
	 *
	 * @param id            empleado a borrar
	 * @param idSolicitante empleado que hace la petición
	 */
	@Transactional
	public void eliminar(Integer id, Integer idSolicitante) {
		if (id.equals(idSolicitante)) {
			throw new ConflictException("No puede eliminar su propia cuenta.");
		}
		empleadoRepository.delete(obtener(id));
		empleadoRepository.flush();
	}

	/**
	 * Guarda el correo de la cuenta y, si llega, la nueva contraseña cifrada.
	 *
	 * @param cuenta cuenta del empleado
	 * @param datos datos nuevos
	 */
	private void guardarCuenta(Cuenta cuenta, EmpleadoAdminDTO datos) {
		cuenta.setCorreo(datos.correoAcceso().trim().toLowerCase(Locale.ROOT));
		if (datos.contrasena() != null) {
			cuenta.setPasswordHash(passwordEncoder.encode(datos.contrasena()));
		}
		cuentaRepository.saveAndFlush(cuenta);
	}

	/**
	 * Copia los datos del DTO al empleado, con los datos propios del vendedor o del técnico.
	 *
	 * @param datos datos nuevos
	 * @param empleado empleado que se modifica
	 */
	private void copiar(EmpleadoAdminDTO datos, Empleado empleado) {
		empleado.setNombres(datos.nombres().trim());
		empleado.setApellidos(datos.apellidos().trim());
		empleado.setFechaContratacion(datos.fechaContratacion());
		empleado.setSalario(datos.salario());
		empleado.setSucursal(sucursalRepository.findById(datos.idSucursal()).orElseThrow(
				() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + ".")));
		empleado.setJefe(datos.idJefe() == null ? null
				: empleadoRepository.findById(datos.idJefe()).orElseThrow(
						() -> new ResourceNotFoundException("No existe el jefe con id " + datos.idJefe() + ".")));
		if (datos.telefonos() != null) {
			empleado.getTelefonos().clear();
			empleado.getTelefonos().addAll(datos.telefonos());
		}
		if (empleado instanceof Vendedor vendedor) {
			if (datos.certificadoVentas() == null || datos.certificadoVentas().isBlank() || datos.comision() == null) {
				throw new BadRequestException("Un vendedor necesita certificado de ventas y comisión.");
			}
			vendedor.setCertificadoVentas(datos.certificadoVentas().trim());
			vendedor.setComision(datos.comision());
		}
		if (empleado instanceof Tecnico tecnico) {
			if (datos.certificadoTecnico() == null || datos.certificadoTecnico().isBlank()
					|| datos.especialidad() == null || datos.especialidad().isBlank()) {
				throw new BadRequestException("Un técnico necesita certificado técnico y especialidad.");
			}
			tecnico.setCertificadoTecnico(datos.certificadoTecnico().trim());
			tecnico.setEspecialidad(datos.especialidad().trim());
		}
	}

	/**
	 * Lanza ConflictException si el correo ya tiene una cuenta.
	 *
	 * @param correo correo que se quiere usar
	 */
	private void validarCorreo(String correo) {
		if (cuentaRepository.existsByCorreoIgnoreCase(correo.trim())) {
			throw new ConflictException("Ese correo ya está registrado.");
		}
	}

	/**
	 * Arma el DTO con el correo de la cuenta.
	 *
	 * @param empleado empleado que se convierte
	 * @return el DTO del empleado
	 */
	private EmpleadoAdminDTO aDTO(Empleado empleado) {
		return EmpleadoAdminDTO.de(empleado,
				cuentaRepository.findByEmpleadoIdEmpleado(empleado.getIdEmpleado()).map(Cuenta::getCorreo).orElse(null));
	}

	/**
	 * Busca el empleado; si no existe lanza ResourceNotFoundException.
	 *
	 * @param id identificador del empleado
	 * @return el empleado
	 */
	private Empleado obtener(Integer id) {
		return empleadoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con id " + id + "."));
	}
}
