package ViveSalud.demo.Model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Table (name = "inscripciones")
@Entity
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    private Long idCampana;

    private LocalDate fechaInscripcion = LocalDate.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCampana() {
        return idCampana;
    }

    public void setIdCampana(Long idCampana) {
        this.idCampana = idCampana;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}
