package ViveSalud.demo.Model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table (name = "citas")
public class Cita {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCita;
    private LocalDate fecha;
    private LocalTime hora;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private EstadoCita estado;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RolUsuario rol;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Usuario medico;

    public Cita() {
    }

    public Cita(Long idCita, LocalDate fecha, LocalTime hora, Usuario paciente, EstadoCita estado, RolUsuario rol, Usuario medico) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.estado = estado;
        this.rol = rol;
        this.medico = medico;
    }

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public Usuario getMedico() {
        return medico;
    }

    public void setMedico(Usuario medico) {
        this.medico = medico;
    }
}
