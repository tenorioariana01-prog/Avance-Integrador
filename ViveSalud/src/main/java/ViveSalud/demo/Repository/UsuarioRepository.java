package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);

    List<Usuario> findByRolUsuario_NombreRol(String nombreRol);

    @Query("SELECT DISTINCT u.especialidad FROM Usuario u WHERE u.rolUsuario.id_rol = 2 AND u.especialidad IS NOT NULL")
    List<String> findDistinctEspecialidadesDeMedicos();

    @Query("SELECT u FROM Usuario u WHERE u.rolUsuario.id_rol = 2 AND u.especialidad = :especialidad")
    List<Usuario> findMedicosPorEspecialidad(@Param("especialidad") String especialidad);


}






