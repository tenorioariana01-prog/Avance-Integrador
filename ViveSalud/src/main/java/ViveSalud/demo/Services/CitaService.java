package ViveSalud.demo.Services;

import ViveSalud.demo.Model.Cita;
import ViveSalud.demo.Model.EstadoCita;
import ViveSalud.demo.Model.HorarioMedico;
import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Repository.CitaRepository;
import ViveSalud.demo.Repository.EstadoCitaRepository;
import ViveSalud.demo.Repository.HorarioMedicoRepository;
import ViveSalud.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HorarioMedicoRepository horarioMedicoRepository;

    @Autowired
    private EstadoCitaRepository estadoCitaRepository;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private TaskScheduler taskScheduler;


    // ============================================================
    //                PROGRAMAR CITA
    // ============================================================
    public Map<String, Object> programarCita(Long idPaciente, Long idMedico, LocalDate fecha, LocalTime hora, String telefonoFormulario) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario paciente = usuarioRepository.findById(idPaciente)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Paciente no encontrado"));

            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Médico no encontrado"));

            // ... validaciones ...

            EstadoCita estadoPendiente = estadoCitaRepository.findByNombreEstadoIgnoreCase("PENDIENTE");
            if (estadoPendiente == null) {
                throw new RuntimeException("No existe el estado 'PENDIENTE'");
            }

            // Crear cita
            Cita nuevaCita = new Cita();
            nuevaCita.setPaciente(paciente);
            nuevaCita.setMedico(medico);
            nuevaCita.setFecha(fecha);
            nuevaCita.setHora(hora);
            nuevaCita.setEstado(estadoPendiente);

            Cita citaGuardada = citaRepository.save(nuevaCita);

            // 📌 Usar el teléfono del formulario si existe, si no el de la BD
            String telefonoDestino = (telefonoFormulario != null && !telefonoFormulario.isBlank())
                    ? telefonoFormulario
                    : paciente.getTelefono();

            if (telefonoDestino != null) {
                String mensaje = "📅 *ViveSalud - Confirmación de Cita*\n" +
                        "Estimado/a " + paciente.getNombres() + ",\n" +
                        "Su cita ha sido programada exitosamente:\n" +
                        "🧑‍⚕️ Médico: " + medico.getNombres() + "\n" +
                        "📅 Fecha: " + fecha + "\n" +
                        "⏰ Hora: " + hora + "\n" +
                        "🏥 Especialidad: " + medico.getEspecialidad() + "\n\n" +
                        "Gracias por confiar en ViveSalud.";

                twilioService.enviarSms(telefonoDestino, mensaje);
            }

            programarRecordatorio(citaGuardada);

            response.put("success", true);
            response.put("message", "Cita creada exitosamente");
            response.put("cita", citaGuardada);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al programar cita: " + e.getMessage());
        }

        return response;
    }

    private void programarRecordatorio(Cita cita) {
        LocalDateTime fechaHoraRecordatorio = LocalDateTime.of(cita.getFecha(), cita.getHora())
                .minusHours(1); // recordatorio 1 hora antes

        Runnable tarea = () -> {
            Usuario paciente = cita.getPaciente();
            Usuario medico = cita.getMedico();

            if (paciente.getTelefono() != null) {
                String mensaje = "⏰ Recordatorio de cita ViveSalud\n" +
                        "Estimado/a " + paciente.getNombres() + ",\n" +
                        "Tiene una cita programada:\n" +
                        "🧑‍⚕️ Médico: " + medico.getNombres() + "\n" +
                        "📅 Fecha: " + cita.getFecha() + "\n" +
                        "⏰ Hora: " + cita.getHora() + "\n\n" +
                        "Por favor llegue 10 minutos antes.";

                twilioService.enviarSms(paciente.getTelefono(), mensaje);
            }
        };

        taskScheduler.schedule(tarea, fechaHoraRecordatorio.atZone(ZoneId.systemDefault()).toInstant());
    }



    // ============================================================
    //              CONSULTAR HORARIOS DISPONIBLES
    // ============================================================
    public Map<String, Object> obtenerHorariosDisponibles(Long idMedico, LocalDate fecha) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Médico no encontrado"));

            String diaSemana = fecha.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es", "PE"))
                    .toLowerCase()
                    .trim();

            List<HorarioMedico> horarios =
                    horarioMedicoRepository.findByMedicoAndDiaSemanaIgnoreCase(idMedico, diaSemana);

            if (horarios.isEmpty()) {
                response.put("success", false);
                response.put("message", "El médico no tiene horarios ese día");
                return response;
            }

            List<Cita> citasDelDia = citaRepository.findByMedicoAndFecha(medico, fecha);

            Set<LocalTime> horasOcupadas = citasDelDia.stream()
                    .filter(c -> !c.getEstado().getNombreEstado().equalsIgnoreCase("CANCELADA"))
                    .map(Cita::getHora)
                    .collect(Collectors.toSet());

            List<Map<String, Object>> horariosDisponibles = new ArrayList<>();

            for (HorarioMedico h : horarios) {
                LocalTime actual = h.getHoraInicio();

                while (actual.isBefore(h.getHoraFin())) {
                    boolean disponible = !horasOcupadas.contains(actual);

                    if (fecha.isEqual(LocalDate.now()) && actual.isBefore(LocalTime.now())) {
                        disponible = false;
                    }

                    if (disponible) { // 👉 solo agregamos si está disponible
                        Map<String, Object> slot = new HashMap<>();
                        slot.put("hora", actual.toString());
                        horariosDisponibles.add(slot);
                    }

                    actual = actual.plusMinutes(30);
                }
            }


            response.put("success", true);
            response.put("horarios", horariosDisponibles);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    // ============================================================
    //                   MODIFICAR CITA
    // ============================================================
    public Map<String, Object> modificarCita(Long idCita, LocalDate nuevaFecha, LocalTime nuevaHora) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cita cita = citaRepository.findById(idCita)
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

            String estadoActual = cita.getEstado().getNombreEstado();

            if (estadoActual.equalsIgnoreCase("CANCELADA") ||
                    estadoActual.equalsIgnoreCase("FINALIZADA")) {

                response.put("success", false);
                response.put("message", "No se puede modificar una cita " + estadoActual);
                return response;
            }

            if (!verificarHorarioDisponible(cita.getMedico(), nuevaFecha, nuevaHora)) {
                response.put("success", false);
                response.put("message", "El horario no está disponible");
                return response;
            }

            cita.setFecha(nuevaFecha);
            cita.setHora(nuevaHora);

            Cita actualizada = citaRepository.save(cita);

            response.put("success", true);
            response.put("message", "Cita modificada exitosamente");
            response.put("cita", actualizada);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    // ============================================================
    //                     CANCELAR CITA
    // ============================================================
    public Map<String, Object> cancelarCita(Long idCita) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cita cita = citaRepository.findById(idCita)
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

            if (cita.getEstado().getNombreEstado().equalsIgnoreCase("CANCELADA")) {
                response.put("success", false);
                response.put("message", "La cita ya está cancelada");
                return response;
            }

            EstadoCita cancelada = estadoCitaRepository.findByNombreEstado("CANCELADA");
            cita.setEstado(cancelada);
            citaRepository.save(cita);

            response.put("success", true);
            response.put("message", "Cita cancelada exitosamente");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    // ============================================================
    //        MÉTODO AUXILIAR — verificar horario disponible
    // ============================================================
    private boolean verificarHorarioDisponible(Usuario medico, LocalDate fecha, LocalTime hora) {

        String diaSemana = fecha.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "PE"))
                .toLowerCase()
                .trim();

        List<HorarioMedico> horarios =
                horarioMedicoRepository.findByMedicoAndDiaSemanaClean(medico, diaSemana);

        if (horarios == null || horarios.isEmpty()) {
            return false;
        }

        for (HorarioMedico h : horarios) {
            if (!hora.isBefore(h.getHoraInicio()) && hora.isBefore(h.getHoraFin())) {
                return true;
            }
        }

        return false;
    }

    public List<Map<String, Object>> listarMedicosDisponibles() {
        List<Usuario> medicos = usuarioRepository.findByRolUsuario_NombreRol("MEDICO");

        List<Map<String, Object>> medicosDto = new ArrayList<>();

        for (Usuario medico : medicos) {
            Map<String, Object> medicoInfo = new HashMap<>();
            medicoInfo.put("id", medico.getIdUsuario());
            medicoInfo.put("nombres", medico.getNombres());
            medicoInfo.put("apellidos", medico.getApellidos());
            medicoInfo.put("especialidad", medico.getEspecialidad());
            medicoInfo.put("nroColegiatura", medico.getNroColegiatura());
            medicosDto.add(medicoInfo);
        }

        return medicosDto;
    }

    public List<Map<String, Object>> obtenerCitasPaciente(Long idPaciente) {
        Usuario paciente = usuarioRepository.findById(idPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        List<Cita> citas = citaRepository.findByPacienteOrderByFechaDescHoraDesc(paciente);

        return citas.stream().map(this::convertirCitaADto).collect(Collectors.toList());
    }
    public List<Map<String, Object>> obtenerCitasMedico(Long idMedico) {
        Usuario medico = usuarioRepository.findById(idMedico)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        LocalDate hoy = LocalDate.now();

        List<Cita> citas = citaRepository.findByMedicoAndFechaGreaterThanEqualOrderByFechaAscHoraAsc(
                medico, hoy);

        return citas.stream().map(this::convertirCitaADto).collect(Collectors.toList());
    }
    public List<String> listarEspecialidades() {
        return usuarioRepository.findDistinctEspecialidadesDeMedicos();
    }
    public List<Usuario> listarMedicosPorEspecialidad(String especialidad) {
        return usuarioRepository.findMedicosPorEspecialidad(especialidad);
    }



    private Map<String, Object> convertirCitaADto(Cita cita) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", cita.getId());
        dto.put("fecha", cita.getFecha().toString());
        dto.put("hora", cita.getHora().toString());

        dto.put("estado", cita.getEstado().getNombreEstado());

        // Info del médico
        Map<String, Object> medicoInfo = new HashMap<>();
        medicoInfo.put("id", cita.getMedico().getIdUsuario());
        medicoInfo.put("nombres", cita.getMedico().getNombres());
        medicoInfo.put("apellidos", cita.getMedico().getApellidos());
        dto.put("medico", medicoInfo);

        // Info del paciente
        Map<String, Object> pacienteInfo = new HashMap<>();
        pacienteInfo.put("id", cita.getPaciente().getIdUsuario());
        pacienteInfo.put("nombres", cita.getPaciente().getNombres());
        pacienteInfo.put("apellidos", cita.getPaciente().getApellidos());
        dto.put("paciente", pacienteInfo);

        return dto;
    }
    public List<Map<String, Object>> obtenerTodasCitasMedico(Long idMedico) {
        Usuario medico = usuarioRepository.findById(idMedico)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        List<Cita> citas = citaRepository.findByMedicoOrderByFechaDescHoraDesc(medico);

        return citas.stream().map(this::convertirCitaADtoCompleto).collect(Collectors.toList());
    }

    public Map<String, Object> obtenerDetalleCita(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        return convertirCitaADtoCompleto(cita);
    }

    public Map<String, Object> finalizarCita(Long idCita, String observaciones) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cita cita = citaRepository.findById(idCita)
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

            String estadoActual = cita.getEstado().getNombreEstado();

            if (estadoActual.equalsIgnoreCase("FINALIZADA")) {
                response.put("success", false);
                response.put("message", "La cita ya está finalizada");
                return response;
            }

            if (estadoActual.equalsIgnoreCase("CANCELADA")) {
                response.put("success", false);
                response.put("message", "No se puede finalizar una cita cancelada");
                return response;
            }

            // Cambiar estado a FINALIZADA
            EstadoCita estadoFinalizada = estadoCitaRepository.findByNombreEstadoIgnoreCase("FINALIZADA");
            if (estadoFinalizada == null) {
                throw new RuntimeException("No existe el estado 'FINALIZADA'");
            }

            cita.setEstado(estadoFinalizada);
            cita.setObservaciones(observaciones);

            Cita citaActualizada = citaRepository.save(cita);

            response.put("success", true);
            response.put("message", "Cita finalizada exitosamente");
            response.put("cita", convertirCitaADtoCompleto(citaActualizada));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    private Map<String, Object> convertirCitaADtoCompleto(Cita cita) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", cita.getId());
        dto.put("fecha", cita.getFecha().toString());
        dto.put("hora", cita.getHora().toString());
        dto.put("motivo", cita.getMotivo());
        dto.put("observaciones", cita.getObservaciones());
        dto.put("estado", cita.getEstado().getNombreEstado());

        // Info completa del médico
        Map<String, Object> medicoInfo = new HashMap<>();
        medicoInfo.put("id", cita.getMedico().getIdUsuario());
        medicoInfo.put("nombres", cita.getMedico().getNombres());
        medicoInfo.put("apellidos", cita.getMedico().getApellidos());
        medicoInfo.put("especialidad", cita.getMedico().getEspecialidad());
        dto.put("medico", medicoInfo);

        // Info completa del paciente
        Map<String, Object> pacienteInfo = new HashMap<>();
        pacienteInfo.put("id", cita.getPaciente().getIdUsuario());
        pacienteInfo.put("nombres", cita.getPaciente().getNombres());
        pacienteInfo.put("apellidos", cita.getPaciente().getApellidos());
        pacienteInfo.put("dni", cita.getPaciente().getDni());
        pacienteInfo.put("telefono", cita.getPaciente().getTelefono());
        pacienteInfo.put("fechaNacimiento", cita.getPaciente().getFechaNacimiento() != null
                ? cita.getPaciente().getFechaNacimiento().toString() : null);
        dto.put("paciente", pacienteInfo);

        return dto;
    }

}
