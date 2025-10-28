package ViveSalud.demo.Model;


import jakarta.persistence.*;

@Entity
@Table (name = "estado_citas")
public class EstadoCita {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;
    private String nombreEstado;

    public EstadoCita() {
    }

    public EstadoCita(Long idEstado, String nombreEstado) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}


