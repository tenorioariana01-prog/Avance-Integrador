package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByNombresAndContrasena(String nombres, String contrasena);
}
