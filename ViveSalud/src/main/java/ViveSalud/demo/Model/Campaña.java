package ViveSalud.demo.Model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "campañas")
public class Campaña {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCampaña;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String lugar;
    private String descripcion;
    private Long idResponsable;
    private Long idTipo;

    public Campaña() {
    }

    public Campaña(Long idCampaña, String nombre, LocalDate fechaFin, LocalDate fechaInicio, String lugar, String descripcion, Long idResponsable, Long idTipo) {
        this.idCampaña = idCampaña;
        this.nombre = nombre;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.idResponsable = idResponsable;
        this.idTipo = idTipo;
    }

    public Long getIdCampaña() {
        return idCampaña;
    }

    public void setIdCampaña(Long idCampaña) {
        this.idCampaña = idCampaña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }
}

