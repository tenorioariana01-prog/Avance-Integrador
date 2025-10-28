package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.Cita;
import ViveSalud.demo.Model.EstadoCita;
import ViveSalud.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita,Long> {
    //   // 🔹 Buscar citas por médico y fecha
      List<Cita> findByMedicoAndFecha(Usuario medico, LocalDate fecha);
    //
    //    // 🔹 Verificar si ya existe una cita en un horario específico (no cancelada)
     @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c " +
                "WHERE c.medico = :medico AND c.fecha = :fecha AND c.hora = :hora " +
                "AND c.estado.nombreEstado != 'CANCELADA'")
        boolean existsCitaEnHorario(@Param("medico") Usuario medico,
                                    @Param("fecha") LocalDate fecha,
                                    @Param("hora") LocalTime hora);
    //
    //    // 🔹 Obtener todas las citas de un paciente por su ID, ordenadas por fecha descendente
        @Query("SELECT c FROM Cita c WHERE c.paciente.idUsuario = :idPaciente ORDER BY c.fecha DESC, c.hora DESC")
        List<Cita> findCitasByPaciente(@Param("idPaciente") Long idPaciente);

    //    // 🔹 Obtener citas por estado
        List<Cita> findByEstado(EstadoCita estado);
    //
        // 🔹 Obtener citas futuras de un médico (ordenadas por fecha y hora)
        @Query("SELECT c FROM Cita c WHERE c.medico = :medico AND " +
                "(c.fecha > :fechaActual OR (c.fecha = :fechaActual AND c.hora >= :horaActual)) " +
                "ORDER BY c.fecha, c.hora")
        List<Cita> findCitasFuturasByMedico(@Param("medico") Usuario medico,
                                            @Param("fechaActual") LocalDate fechaActual,
                                            @Param("horaActual") LocalTime horaActual);
}


