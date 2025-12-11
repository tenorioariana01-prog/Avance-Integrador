package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstadoCitaRepository extends JpaRepository<EstadoCita, Long> {

    // 🔹 Buscar estado por nombre, ignorando mayúsculas, minúsculas y espacios
    @Query("SELECT e FROM EstadoCita e " +
            "WHERE TRIM(UPPER(e.nombreEstado)) = TRIM(UPPER(:nombreEstado))")
    EstadoCita findByNombreEstadoIgnoreCase(@Param("nombreEstado") String nombreEstado);

    // 🔹 Buscar solo por nombre (versión simple)
    EstadoCita findByNombreEstado(String nombreEstado);

    // 🔹 Verificar si ya existe un estado con ese nombre (ignorando mayúsculas)
    @Query("SELECT COUNT(e) > 0 FROM EstadoCita e " +
            "WHERE UPPER(e.nombreEstado) = UPPER(:nombreEstado)")
    boolean existsByNombreEstadoIgnoreCase(@Param("nombreEstado") String nombreEstado);
}
