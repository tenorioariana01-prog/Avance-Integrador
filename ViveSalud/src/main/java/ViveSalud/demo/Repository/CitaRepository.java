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

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // =======================================================
    // 1️⃣ Buscar citas por médico y fecha
    // =======================================================
    List<Cita> findByMedicoAndFecha(Usuario medico, LocalDate fecha);

    List<Cita> findByPacienteOrderByFechaDescHoraDesc(Usuario paciente);

    List<Cita> findByMedicoAndFechaGreaterThanEqualOrderByFechaAscHoraAsc(
            Usuario medico, LocalDate fecha
    );

    List<Cita> findByMedicoOrderByFechaDescHoraDesc(Usuario medico);


    // =======================================================
    // 2️⃣ Verificar si existe una cita activa en un horario específico
    //    (no CANCELADA) — usado para crear y modificar citas
    // =======================================================
    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Cita c
        WHERE c.medico = :medico
          AND c.fecha = :fecha
          AND c.hora = :hora
          AND UPPER(TRIM(c.estado.nombreEstado)) <> 'CANCELADA'
    """)
    boolean existsCitaActivaEnHorario(
            @Param("medico") Usuario medico,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    // =======================================================
    // 3️⃣ Obtener citas por paciente (ordenadas más recientes primero)
    // =======================================================
    @Query("""
        SELECT c 
        FROM Cita c 
        WHERE c.paciente.idUsuario = :idPaciente
        ORDER BY c.fecha DESC, c.hora DESC
    """)
    List<Cita> findCitasByPaciente(@Param("idPaciente") Long idPaciente);

    // =======================================================
    // 4️⃣ Obtener citas por estado (objeto EstadoCita)
    // =======================================================
    List<Cita> findByEstado(EstadoCita estado);

    // =======================================================
    // 5️⃣ Citas futuras de un médico (solo activas, no canceladas)
    // =======================================================
    @Query("""
        SELECT c
        FROM Cita c
        WHERE c.medico = :medico
          AND UPPER(TRIM(c.estado.nombreEstado)) <> 'CANCELADA'
          AND (
                c.fecha > :fechaActual
             OR (c.fecha = :fechaActual AND c.hora >= :horaActual)
          )
        ORDER BY c.fecha, c.hora
    """)
    List<Cita> findCitasFuturasByMedico(
            @Param("medico") Usuario medico,
            @Param("fechaActual") LocalDate fechaActual,
            @Param("horaActual") LocalTime horaActual
    );

    // =======================================================
    // 6️⃣ Obtener citas del médico en una fecha EXCLUYENDO canceladas
    //    (para reprogramación y disponibilidad)
    // =======================================================
    @Query("""
        SELECT c
        FROM Cita c
        WHERE c.medico = :medico
          AND c.fecha = :fecha
          AND UPPER(TRIM(c.estado.nombreEstado)) <> 'CANCELADA'
        ORDER BY c.hora
    """)
    List<Cita> findCitasActivasByMedicoAndFecha(
            @Param("medico") Usuario medico,
            @Param("fecha") LocalDate fecha
    );
}
