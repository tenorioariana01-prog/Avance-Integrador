package ViveSalud.demo.Model;


import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table (name = "horarios_medico")
public class HorarioMedico {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Usuario medico;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String diaSemana;

    public HorarioMedico() {
    }


    public HorarioMedico(Long idHorario, Usuario medico, LocalTime horaInicio, LocalTime horaFin, String diaSemana) {
        this.idHorario = idHorario;
        this.medico = medico;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.diaSemana = diaSemana;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public Usuario getMedico() {
        return medico;
    }

    public void setMedico(Usuario medico) {
        this.medico = medico;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }
}
