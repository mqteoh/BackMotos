package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reparacion")
public class Reparacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reparacion")
	private Integer idReparacion;

	@Column(name = "fecha_agendada", nullable = false)
	private LocalDateTime fechaAgendada;

	@Column(name = "fecha_finalizacion")
	private LocalDateTime fechaFinalizacion;

	@Column(name = "descripcion", nullable = false, length = 255)
	private String descripcion;

	@Column(name = "kilometraje_ingreso")
	private Integer kilometrajeIngreso;

	@Column(name = "costo", precision = 12, scale = 2)
	private BigDecimal costo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 12)
	private EstadoReparacion estado = EstadoReparacion.AGENDADA;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "placa")
	private Moto moto;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tecnico")
	private Tecnico tecnico;

	@OneToMany(mappedBy = "reparacion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RepuestoReparacion> repuestos = new ArrayList<>();

	public Reparacion() {
	}

	public RepuestoReparacion agregarRepuesto(Repuesto repuesto, Integer cantidad, BigDecimal precioUnitario) {
		RepuestoReparacion uso = new RepuestoReparacion(this, repuesto, cantidad, precioUnitario);
		repuestos.add(uso);
		return uso;
	}

	public BigDecimal getCostoRepuestos() {
		return repuestos.stream().map(RepuestoReparacion::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getCostoTotal() {
		return (costo == null ? BigDecimal.ZERO : costo).add(getCostoRepuestos());
	}

	public Integer getIdReparacion() {
		return idReparacion;
	}

	public void setIdReparacion(Integer idReparacion) {
		this.idReparacion = idReparacion;
	}

	public LocalDateTime getFechaAgendada() {
		return fechaAgendada;
	}

	public void setFechaAgendada(LocalDateTime fechaAgendada) {
		this.fechaAgendada = fechaAgendada;
	}

	public LocalDateTime getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getKilometrajeIngreso() {
		return kilometrajeIngreso;
	}

	public void setKilometrajeIngreso(Integer kilometrajeIngreso) {
		this.kilometrajeIngreso = kilometrajeIngreso;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public EstadoReparacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoReparacion estado) {
		this.estado = estado;
	}

	public Moto getMoto() {
		return moto;
	}

	public void setMoto(Moto moto) {
		this.moto = moto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	public List<RepuestoReparacion> getRepuestos() {
		return repuestos;
	}

	public void setRepuestos(List<RepuestoReparacion> repuestos) {
		this.repuestos = repuestos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Reparacion otra)) {
			return false;
		}
		return idReparacion != null && idReparacion.equals(otra.idReparacion);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idReparacion);
	}
}
