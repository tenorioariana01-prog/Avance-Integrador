package ViveSalud.demo.Controller;


import ViveSalud.demo.Model.Cita;
import ViveSalud.demo.Services.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping ("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    /**
     * 📋 Listar todos los médicos disponibles con sus especialidades
     * GET /api/citas/medicos
     */
    @GetMapping("/medicos")
    public ResponseEntity<List<Map<String, Object>>> listarMedicos() {
        try {
            List<Map<String, Object>> medicos = citaService.listarMedicosDisponibles();
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            System.err.println("❌ Error en controller listarMedicos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 🕐 Obtener horarios disponibles de un médico en una fecha específica
     * GET /api/citas/horarios-disponibles?idMedico=1&fecha=2025-11-01
     */
    @GetMapping("/horarios-disponibles")
    public ResponseEntity<Map<String, Object>> obtenerHorariosDisponibles(
            @RequestParam Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        try {
            // Obtener el día de la semana en minúscula y sin espacios
            String diaSemana = fecha.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es", "PE"))
                    .toLowerCase()
                    .trim();

            System.out.println("🗓️ Consultando horarios del médico " + idMedico + " para el día " + diaSemana);

            Map<String, Object> response = citaService.obtenerHorariosDisponibles(idMedico, fecha);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener horarios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    /**
     * ➕ Programar una nueva cita médica
     * POST /api/citas/programar
     */
    @PostMapping("/programar")
    public ResponseEntity<Map<String, Object>> programarCita(@RequestBody Map<String, Object> citaData) {
        try {
            Long idPaciente = Long.valueOf(citaData.get("idPaciente").toString());
            Long idMedico = Long.valueOf(citaData.get("idMedico").toString());
            LocalDate fecha = LocalDate.parse(citaData.get("fecha").toString());
            LocalTime hora = LocalTime.parse(citaData.get("hora").toString());

            Map<String, Object> response = citaService.programarCita(idPaciente, idMedico, fecha, hora);

            if ((Boolean) response.get("success")) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en los datos enviados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * ✏️ Modificar una cita existente (reprogramar)
     * PUT /api/citas/modificar/{idCita}
     */
    @PutMapping("/modificar/{idCita}")
    public ResponseEntity<Map<String, Object>> modificarCita(
            @PathVariable Long idCita,
            @RequestBody Map<String, String> datos) {

        try {
            LocalDate nuevaFecha = LocalDate.parse(datos.get("fecha"));
            LocalTime nuevaHora = LocalTime.parse(datos.get("hora"));

            Map<String, Object> response = citaService.modificarCita(idCita, nuevaFecha, nuevaHora);

            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al modificar la cita: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * ❌ Cancelar una cita
     * DELETE /api/citas/cancelar/{idCita}
     */
    @DeleteMapping("/cancelar/{idCita}")
    public ResponseEntity<Map<String, Object>> cancelarCita(@PathVariable Long idCita) {
        try {
            Map<String, Object> response = citaService.cancelarCita(idCita);

            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al cancelar la cita: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 👤 Obtener todas las citas de un paciente
     * GET /api/citas/paciente/{idPaciente}
     */
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Cita>> obtenerCitasPaciente(@PathVariable Long idPaciente) {
        try {
            List<Cita> citas = citaService.obtenerCitasPaciente(idPaciente);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas del paciente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 👨‍⚕️ Obtener todas las citas futuras de un médico
     * GET /api/citas/medico/{idMedico}
     */
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Cita>> obtenerCitasMedico(@PathVariable Long idMedico) {
        try {
            List<Cita> citas = citaService.obtenerCitasMedico(idMedico);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas del médico: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
