package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario,Long> {
    RolUsuario findByNombreRol(String nombreRol);
}
