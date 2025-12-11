package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.HorarioMedico;
import ViveSalud.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {

    // Obtener todos los horarios de un médico
    List<HorarioMedico> findByMedico(Usuario medico);

    // Obtener horarios usando solo el ID del médico
    List<HorarioMedico> findByMedico_IdUsuario(Long idMedico);

    // Buscar horarios EXACTOS por día (ignorando espacios y mayúsculas)
    @Query("SELECT h FROM HorarioMedico h " +
            "WHERE h.medico.idUsuario = :idMedico " +
            "AND TRIM(LOWER(h.diaSemana)) = TRIM(LOWER(:diaSemana))")
    List<HorarioMedico> findByMedicoAndDiaSemanaIgnoreCase(
            @Param("idMedico") Long idMedico,
            @Param("diaSemana") String diaSemana
    );

    // Buscar horarios cuando quieres que el día CONTENGA texto (ej: "martes" dentro de "Martes")
    @Query("SELECT h FROM HorarioMedico h " +
            "WHERE h.medico = :medico " +
            "AND LOWER(TRIM(h.diaSemana)) LIKE CONCAT('%', LOWER(TRIM(:diaSemana)), '%')")
    List<HorarioMedico> findByMedicoAndDiaSemanaClean(
            @Param("medico") Usuario medico,
            @Param("diaSemana") String diaSemana
    );
}
