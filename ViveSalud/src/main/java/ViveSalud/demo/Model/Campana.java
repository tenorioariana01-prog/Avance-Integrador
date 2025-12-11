package ViveSalud.demo.Model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "campañas")
public class Campana {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCampana;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String lugar;
    private String descripcion;
    private Long idResponsable;
    private String imagen;
    @Column(length = 5000)          // detalle puede ser largo, mejor ampliar
    private String detalle;

    public Campana() {
    }

    public Campana(Long idCampana, String nombre, LocalDate fechaInicio, LocalDate fechaFin, String lugar, String descripcion, Long idResponsable,  String imagen, String detalle) {
        this.idCampana = idCampana;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.idResponsable = idResponsable;
        this.imagen = imagen;
        this.detalle = detalle;
    }

    public Long getIdCampana() {
        return idCampana;
    }

    public void setIdCampana(Long idCampana) {
        this.idCampana = idCampana;
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


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}

