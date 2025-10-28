package ViveSalud.demo.Controller;


import ViveSalud.demo.Model.HorarioMedico;
import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Repository.HorarioMedicoRepository;
import ViveSalud.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/horarios")
public class HorarioMedicoController {

    @Autowired
    private HorarioMedicoRepository horarioMedicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * ➕ Registrar un nuevo horario para un médico
     * POST /api/horarios-medico/registrar
     * Body JSON:
     * {
     *   "idMedico": 2,
     *   "diaSemana": "lunes",
     *   "horaInicio": "08:00",
     *   "horaFin": "12:00"
     * }
     */
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarHorario(@RequestBody Map<String, Object> datos) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long idMedico = Long.valueOf(datos.get("idMedico").toString());
            String diaSemana = datos.get("diaSemana").toString().toLowerCase();
            LocalTime horaInicio = LocalTime.parse(datos.get("horaInicio").toString());
            LocalTime horaFin = LocalTime.parse(datos.get("horaFin").toString());

            Usuario medico = usuarioRepository.findById(idMedico)
                    .orElseThrow(() -> new IllegalArgumentException("El médico con ID " + idMedico + " no existe."));

            // Validar que el usuario tenga rol MEDICO
            if (!medico.getRolUsuario().getNombreRol().equalsIgnoreCase("MEDICO")) {
                throw new IllegalArgumentException("El usuario no tiene rol de MÉDICO.");
            }

            HorarioMedico nuevoHorario = new HorarioMedico();
            nuevoHorario.setMedico(medico);
            nuevoHorario.setDiaSemana(diaSemana);
            nuevoHorario.setHoraInicio(horaInicio);
            nuevoHorario.setHoraFin(horaFin);

            horarioMedicoRepository.save(nuevoHorario);

            response.put("success", true);
            response.put("message", "Horario registrado correctamente.");
            response.put("horario", nuevoHorario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar horario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 📋 Listar todos los horarios de un médico
     * GET /api/horarios-medico/{idMedico}
     */
    @GetMapping("/{idMedico}")
    public ResponseEntity<?> listarHorariosPorMedico(@PathVariable Long idMedico) {
        try {
            List<HorarioMedico> horarios = horarioMedicoRepository.findByMedico_IdUsuario(idMedico);

            if (horarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No se encontraron horarios para este médico."));
            }

            return ResponseEntity.ok(horarios);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al listar horarios: " + e.getMessage()));
        }
    }

    /**
     * ❌ Eliminar un horario de médico
     * DELETE /api/horarios-medico/eliminar/{idHorario}
     */
    @DeleteMapping("/eliminar/{idHorario}")
    public ResponseEntity<Map<String, Object>> eliminarHorario(@PathVariable Long idHorario) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (!horarioMedicoRepository.existsById(idHorario)) {
                response.put("success", false);
                response.put("message", "No existe un horario con el ID especificado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            horarioMedicoRepository.deleteById(idHorario);
            response.put("success", true);
            response.put("message", "Horario eliminado correctamente.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar horario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
