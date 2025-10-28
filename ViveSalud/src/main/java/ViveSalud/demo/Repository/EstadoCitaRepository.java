package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstadoCitaRepository extends JpaRepository<EstadoCita,Long> {
    // Buscar estado ignorando mayúsculas/minúsculas y espacios al inicio/final
    @Query("SELECT e FROM EstadoCita e WHERE TRIM(UPPER(e.nombreEstado)) = TRIM(UPPER(:nombreEstado))")
    EstadoCita findByNombreEstadoIgnoreCase(@Param("nombreEstado") String nombreEstado);
}
