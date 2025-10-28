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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    // ========== PROGRAMAR CITA ==========
    public Map<String, Object> programarCita(Long idPaciente, Long idMedico, LocalDate fecha, LocalTime hora) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Validar que el paciente existe
            Usuario paciente = usuarioRepository.findById(idPaciente)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Paciente no encontrado"));

            // 2. Validar que el médico existe
            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Médico no encontrado"));

            // 3. Verificar que el usuario es médico
            if (!medico.getRolUsuario().getNombreRol().equalsIgnoreCase("MEDICO")) {
                response.put("success", false);
                response.put("message", "El usuario seleccionado no es un médico");
                return response;
            }

            // 4. Validar que la fecha no sea en el pasado
            LocalDate hoy = LocalDate.now();
            if (fecha.isBefore(hoy)) {
                response.put("success", false);
                response.put("message", "No se pueden programar citas en fechas pasadas");
                return response;
            }

            // 5. Si es hoy, validar que la hora no haya pasado
            if (fecha.isEqual(hoy) && hora.isBefore(LocalTime.now())) {
                response.put("success", false);
                response.put("message", "No se pueden programar citas en horarios que ya pasaron");
                return response;
            }

            // 6. Verificar que el horario está dentro del rango del médico
            if (!verificarHorarioDisponible(medico, fecha, hora)) {
                response.put("success", false);
                response.put("message", "El horario seleccionado no está dentro de los horarios del médico");
                return response;
            }

            // 7. Verificar que no exista una cita en ese horario
            if (citaRepository.existsCitaEnHorario(medico, fecha, hora)) {
                response.put("success", false);
                response.put("message", "Ya existe una cita programada en ese horario");
                return response;
            }

            // 8. Crear la nueva cita
            Cita nuevaCita = new Cita();
            nuevaCita.setFecha(fecha);
            nuevaCita.setHora(hora);
            nuevaCita.setMedico(medico);
            nuevaCita.setPaciente(paciente);
            nuevaCita.setRol(paciente.getRolUsuario());

            // 9. Asignar estado "PENDIENTE"
            EstadoCita estadoPendiente = estadoCitaRepository.findByNombreEstadoIgnoreCase("PENDIENTE");
            if (estadoPendiente == null) {
                throw new IllegalStateException("⚠️ Estado 'PENDIENTE' no encontrado en la base de datos");
            }
            nuevaCita.setEstado(estadoPendiente);

            // 10. Guardar la cita
            Cita citaGuardada = citaRepository.save(nuevaCita);

            response.put("success", true);
            response.put("message", "✅ Cita programada exitosamente");
            response.put("cita", citaGuardada);
            System.out.println("✅ Cita programada: " + citaGuardada.getIdCita());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al programar la cita: " + e.getMessage());
            System.err.println("❌ Error al programar cita: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    // ========== OBTENER HORARIOS DISPONIBLES ==========
    public Map<String, Object> obtenerHorariosDisponibles(Long idMedico, LocalDate fecha) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Validar que el médico existe
            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Médico no encontrado"));

            // 2. Obtener día de la semana en español, en minúsculas y sin espacios
            String diaSemana = fecha.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es", "PE"))
                    .toLowerCase()
                    .trim();  // "lunes", "martes", etc.

            // 3. Obtener horarios configurados del médico para ese día
            List<HorarioMedico> horariosMedico = horarioMedicoRepository.findByMedicoAndDiaSemanaIgnoreCase(idMedico, diaSemana);

            if (horariosMedico.isEmpty()) {
                response.put("success", false);
                response.put("message", "El médico no tiene horarios configurados");
                return response;
            }

            // 4. Obtener citas ya programadas para ese día (excepto canceladas)
            EstadoCita estadoCancelada = estadoCitaRepository.findByNombreEstadoIgnoreCase("CANCELADA");
            List<Cita> citasDelDia = citaRepository.findByMedicoAndFecha(medico, fecha);

            // Filtrar citas canceladas
            if (estadoCancelada != null) {
                citasDelDia = citasDelDia.stream()
                        .filter(c -> !c.getEstado().equals(estadoCancelada))
                        .collect(Collectors.toList());
            }

            // 5. Crear conjunto de horas ocupadas
            Set<LocalTime> horasOcupadas = citasDelDia.stream()
                    .map(Cita::getHora)
                    .collect(Collectors.toSet());

            // 6. Generar lista de horarios disponibles (slots de 30 minutos)
            List<Map<String, Object>> horariosDisponibles = new ArrayList<>();
            for (HorarioMedico horario : horariosMedico) {
                LocalTime horaActual = horario.getHoraInicio();
                LocalTime horaFin = horario.getHoraFin();

                while (horaActual.isBefore(horaFin)) {
                    boolean disponible = !horasOcupadas.contains(horaActual);

                    // Si es hoy, no mostrar horarios que ya pasaron
                    if (fecha.isEqual(LocalDate.now()) && horaActual.isBefore(LocalTime.now())) {
                        disponible = false;
                    }

                    Map<String, Object> horarioInfo = new HashMap<>();
                    horarioInfo.put("hora", horaActual.toString());
                    horarioInfo.put("disponible", disponible);
                    horariosDisponibles.add(horarioInfo);

                    horaActual = horaActual.plusMinutes(30); // duración de la consulta
                }
            }

            // 7. Preparar respuesta final
            response.put("success", true);
            response.put("fecha", fecha.toString());
            response.put("medico", medico.getNombres() + " " + medico.getApellidos());
            response.put("especialidad", medico.getEspecialidad());
            response.put("horarios", horariosDisponibles);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener horarios: " + e.getMessage());
            System.err.println("❌ Error al obtener horarios: " + e.getMessage());
        }

        return response;
    }

    // ========== MODIFICAR CITA (Reprogramar) ==========
    public Map<String, Object> modificarCita(Long idCita, LocalDate nuevaFecha, LocalTime nuevaHora) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Buscar la cita existente
            Cita cita = citaRepository.findById(idCita)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Cita no encontrada"));

            // 2. Verificar que la cita no esté cancelada o finalizada
            String estadoActual = cita.getEstado().getNombreEstado().toUpperCase();
            if (estadoActual.equals("CANCELADA") || estadoActual.equals("FINALIZADA")) {
                response.put("success", false);
                response.put("message", "No se puede modificar una cita " + estadoActual.toLowerCase());
                return response;
            }

            // 3. Validar nueva fecha (no puede ser en el pasado)
            if (nuevaFecha.isBefore(LocalDate.now())) {
                response.put("success", false);
                response.put("message", "No se puede reprogramar a una fecha pasada");
                return response;
            }

            // 4. Verificar disponibilidad del nuevo horario
            if (!verificarHorarioDisponible(cita.getMedico(), nuevaFecha, nuevaHora)) {
                response.put("success", false);
                response.put("message", "El nuevo horario no está disponible para este médico");
                return response;
            }

            // 5. Verificar que no exista otra cita en ese horario
            if (citaRepository.existsCitaEnHorario(cita.getMedico(), nuevaFecha, nuevaHora)) {
                response.put("success", false);
                response.put("message", "Ya existe una cita programada en el nuevo horario");
                return response;
            }

            // 6. Actualizar la cita
            cita.setFecha(nuevaFecha);
            cita.setHora(nuevaHora);

            Cita citaActualizada = citaRepository.save(cita);

            response.put("success", true);
            response.put("message", "✅ Cita modificada exitosamente");
            response.put("cita", citaActualizada);
            System.out.println("✅ Cita modificada: " + citaActualizada.getIdCita());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al modificar la cita: " + e.getMessage());
            System.err.println("❌ Error al modificar cita: " + e.getMessage());
        }

        return response;
    }

    // ========== CANCELAR CITA ==========
    public Map<String, Object> cancelarCita(Long idCita) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Buscar la cita
            Cita cita = citaRepository.findById(idCita)
                    .orElseThrow(() -> new IllegalArgumentException("❌ Cita no encontrada"));

            // 2. Verificar que la cita no esté ya cancelada
            if (cita.getEstado().getNombreEstado().equalsIgnoreCase("CANCELADA")) {
                response.put("success", false);
                response.put("message", "La cita ya está cancelada");
                return response;
            }

            // 3. Cambiar estado a CANCELADA
            EstadoCita estadoCancelada = estadoCitaRepository.findByNombreEstadoIgnoreCase("CANCELADA");
            if (estadoCancelada == null) {
                throw new IllegalStateException("⚠️ Estado 'CANCELADA' no encontrado en la base de datos");
            }

            cita.setEstado(estadoCancelada);
            citaRepository.save(cita);

            response.put("success", true);
            response.put("message", "✅ Cita cancelada exitosamente");
            System.out.println("✅ Cita cancelada: " + idCita);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cancelar la cita: " + e.getMessage());
            System.err.println("❌ Error al cancelar cita: " + e.getMessage());
        }

        return response;
    }

    // ========== LISTAR MÉDICOS DISPONIBLES ==========
    public List<Map<String, Object>> listarMedicosDisponibles() {
        List<Map<String, Object>> medicos = new ArrayList<>();

        try {
            // Buscar todos los usuarios con rol MEDICO
            List<Usuario> medicosLista = usuarioRepository.findAll().stream()
                    .filter(u -> u.getRolUsuario() != null &&
                            u.getRolUsuario().getNombreRol().equalsIgnoreCase("MEDICO"))
                    .collect(Collectors.toList());

            for (Usuario medico : medicosLista) {
                Map<String, Object> medicoInfo = new HashMap<>();
                medicoInfo.put("idMedico", medico.getIdUsuario());
                medicoInfo.put("nombres", medico.getNombres());
                medicoInfo.put("apellidos", medico.getApellidos());
                medicoInfo.put("especialidad", medico.getEspecialidad());
                medicoInfo.put("nroColegiatura", medico.getNroColegiatura());

                // Verificar si tiene horarios configurados
                List<HorarioMedico> horarios = horarioMedicoRepository.findByMedico(medico);
                medicoInfo.put("tieneHorarios", !horarios.isEmpty());

                medicos.add(medicoInfo);
            }

            System.out.println("✅ Listado de médicos obtenido: " + medicos.size() + " médicos");

        } catch (Exception e) {
            System.err.println("❌ Error al listar médicos: " + e.getMessage());
        }

        return medicos;
    }

    // ========== OBTENER CITAS DE UN PACIENTE ==========
    public List<Cita> obtenerCitasPaciente(Long idPaciente) {
        try {
            List<Cita> citas = citaRepository.findCitasByPaciente(idPaciente);
            System.out.println("✅ Citas del paciente " + idPaciente + ": " + citas.size());
            return citas;
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas del paciente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========== OBTENER CITAS DE UN MÉDICO ==========
    public List<Cita> obtenerCitasMedico(Long idMedico) {
        try {
            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

            LocalDate hoy = LocalDate.now();
            LocalTime horaActual = LocalTime.now();

            return citaRepository.findCitasFuturasByMedico(medico, hoy, horaActual);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas del médico: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean verificarHorarioDisponible(Usuario medico, LocalDate fecha, LocalTime hora) {
        // Obtener el día de la semana en español
        String diaSemana = obtenerDiaSemanaEspañol(fecha).toLowerCase().trim(); // minúsculas y sin espacios

        System.out.println("🔹 Buscando horarios del médico " + medico.getIdUsuario() + " para el día: " + diaSemana);

        // Buscar los horarios del médico para ese día
        List<HorarioMedico> horariosMedico = horarioMedicoRepository.findByMedicoAndDiaSemanaClean(medico, diaSemana);

        if (horariosMedico == null || horariosMedico.isEmpty()) {
            System.out.println("❌ No se encontraron horarios para el día " + diaSemana);
            return false;
        }

        for (HorarioMedico horario : horariosMedico) {
            System.out.println("⏰ Horario disponible: " + horario.getHoraInicio() + " - " + horario.getHoraFin());
            if (!hora.isBefore(horario.getHoraInicio()) && hora.isBefore(horario.getHoraFin())) {
                return true;
            }
        }

        return false;
    }

    // ========== MÉTODO AUXILIAR: Convertir día de la semana a español ==========
    private String obtenerDiaSemanaEspañol(LocalDate fecha) { switch (fecha.getDayOfWeek())
    { case MONDAY: return "lunes"; case TUESDAY: return "martes"; case WEDNESDAY: return "miércoles"; case THURSDAY: return "jueves"; case FRIDAY: return "viernes"; case SATURDAY: return "sábado"; case SUNDAY: return "domingo"; default: return ""; } }
}



