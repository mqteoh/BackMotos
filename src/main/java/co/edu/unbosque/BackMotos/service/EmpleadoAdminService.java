package co.edu.unbosque.BackMotos.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.EmpleadoAdminDTO;
import co.edu.unbosque.BackMotos.entity.Empleado;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.EmpleadoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;

@Service
public class EmpleadoAdminService {

	private final EmpleadoRepository empleadoRepository;
	private final ClienteRepository clienteRepository;
	private final SucursalRepository sucursalRepository;
	private final PasswordEncoder passwordEncoder;

	public EmpleadoAdminService(EmpleadoRepository empleadoRepository, ClienteRepository clienteRepository,
			SucursalRepository sucursalRepository, PasswordEncoder passwordEncoder) {
		this.empleadoRepository = empleadoRepository;
		this.clienteRepository = clienteRepository;
		this.sucursalRepository = sucursalRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<EmpleadoAdminDTO> listar(Integer idSucursal, TipoEmpleado tipo) {
		return empleadoRepository.findAll().stream()
				.filter(e -> idSucursal == null
						|| (e.getSucursal() != null && e.getSucursal().getIdSucursal().equals(idSucursal)))
				.filter(e -> tipo == null || e.getTipoEmpleado() == tipo)
				.sorted(Comparator.comparing(Empleado::getApellidos).thenComparing(Empleado::getNombres))
				.map(EmpleadoAdminDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public EmpleadoAdminDTO buscar(Integer id) {
		return EmpleadoAdminDTO.de(obtener(id));
	}

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
		return EmpleadoAdminDTO.de(empleadoRepository.saveAndFlush(empleado));
	}

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
		if (!empleado.getCorreoAcceso().equalsIgnoreCase(datos.correoAcceso().trim())) {
			validarCorreo(datos.correoAcceso());
		}
		if (datos.idJefe() != null && datos.idJefe().equals(id)) {
			throw new BadRequestException("Un empleado no puede ser su propio jefe.");
		}
		copiar(datos, empleado);
		return EmpleadoAdminDTO.de(empleadoRepository.saveAndFlush(empleado));
	}

	@Transactional
	public void eliminar(Integer id, Integer idSolicitante) {
		if (id.equals(idSolicitante)) {
			throw new ConflictException("No puede eliminar su propia cuenta.");
		}
		empleadoRepository.delete(obtener(id));
		empleadoRepository.flush();
	}

	private void copiar(EmpleadoAdminDTO datos, Empleado empleado) {
		if (datos.tipoEmpleado() != TipoEmpleado.DUENO && datos.idSucursal() == null) {
			throw new BadRequestException("Todo empleado, salvo el dueño, debe tener sede.");
		}
		empleado.setNombres(datos.nombres().trim());
		empleado.setApellidos(datos.apellidos().trim());
		empleado.setFechaContratacion(datos.fechaContratacion());
		empleado.setCorreoAcceso(datos.correoAcceso().trim().toLowerCase(Locale.ROOT));
		empleado.setSucursal(datos.idSucursal() == null ? null
				: sucursalRepository.findById(datos.idSucursal()).orElseThrow(
						() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + ".")));
		empleado.setJefe(datos.idJefe() == null ? null
				: empleadoRepository.findById(datos.idJefe()).orElseThrow(
						() -> new ResourceNotFoundException("No existe el jefe con id " + datos.idJefe() + ".")));
		if (datos.contrasena() != null) {
			empleado.setPasswordHash(passwordEncoder.encode(datos.contrasena()));
		}
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

	private void validarCorreo(String correo) {
		String normalizado = correo.trim();
		if (empleadoRepository.existsByCorreoAccesoIgnoreCase(normalizado)
				|| clienteRepository.existsByCorreoAccesoIgnoreCase(normalizado)) {
			throw new ConflictException("Ese correo ya está registrado.");
		}
	}

	private Empleado obtener(Integer id) {
		return empleadoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con id " + id + "."));
	}
}
