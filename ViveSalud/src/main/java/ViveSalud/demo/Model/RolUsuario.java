package ViveSalud.demo.Model;


import jakarta.persistence.*;

@Entity
@Table (name = "rol_usuarios")
public class RolUsuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id_rol;

    @Column(name = "nombre_rol")
    private String nombreRol;

    public RolUsuario() {
    }

    public RolUsuario(Long id_rol, String nombreRol) {
        this.id_rol = id_rol;
        this.nombreRol = nombreRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }
}
