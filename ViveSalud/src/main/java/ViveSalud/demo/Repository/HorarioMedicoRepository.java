package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.HorarioMedico;
import ViveSalud.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioMedicoRepository extends JpaRepository <HorarioMedico,Long> {

    // Obtener todos los horarios de un médico específico
    List<HorarioMedico> findByMedico(Usuario medico);

    // Obtener horarios por ID de médico
    List<HorarioMedico> findByMedico_IdUsuario(Long idMedico);



    // Buscar horarios de un médico para un día, ignorando mayúsculas/minúsculas y espacios
    @Query("SELECT h FROM HorarioMedico h WHERE h.medico.idUsuario = :idMedico AND TRIM(LOWER(h.diaSemana)) = :diaSemana")
    List<HorarioMedico> findByMedicoAndDiaSemanaIgnoreCase(@Param("idMedico") Long idMedico,
                                                           @Param("diaSemana") String diaSemana);

    // Buscar horarios de un médico para un día, ignorando mayúsculas/minúsculas y espacios
    @Query("SELECT h FROM HorarioMedico h WHERE h.medico = :medico AND LOWER(TRIM(h.diaSemana)) LIKE CONCAT('%', LOWER(TRIM(:diaSemana)), '%')")
    List<HorarioMedico> findByMedicoAndDiaSemanaClean(@Param("medico") Usuario medico,
                                                      @Param("diaSemana") String diaSemana);

}


