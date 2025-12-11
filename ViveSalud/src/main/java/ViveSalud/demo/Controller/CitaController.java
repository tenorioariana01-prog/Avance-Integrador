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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin (origins = "*")
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // =========================================================
    // 1️⃣ LISTAR MÉDICOS
    // =========================================================
    @GetMapping("/medicos")
    public ResponseEntity<?> listarMedicos() {
        try {
            return ResponseEntity.ok(citaService.listarMedicosDisponibles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al listar médicos"));
        }
    }

    // =========================================================
    // 2️⃣ HORARIOS DISPONIBLES
    // =========================================================
    @GetMapping("/horarios-disponibles")
    public ResponseEntity<?> obtenerHorariosDisponibles(
            @RequestParam Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        try {
            Map<String, Object> response = citaService.obtenerHorariosDisponibles(idMedico, fecha);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al obtener horarios: " + e.getMessage()));
        }
    }

    @GetMapping("/especialidades")
    public ResponseEntity<?> listarEspecialidades() {
        try {
            return ResponseEntity.ok(citaService.listarEspecialidades());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al listar especialidades"));
        }
    }

    @GetMapping("/medicos-por-especialidad")
    public ResponseEntity<?> listarMedicosPorEspecialidad(@RequestParam String especialidad) {
        try {
            return ResponseEntity.ok(citaService.listarMedicosPorEspecialidad(especialidad));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al listar médicos"));
        }
    }



    // =========================================================
    // 3️⃣ PROGRAMAR CITA
    // =========================================================
    @PostMapping("/programar")
    public ResponseEntity<?> programarCita(@RequestBody Map<String, Object> citaData) {

        try {
            Long idPaciente = Long.parseLong(citaData.get("idPaciente").toString());
            Long idMedico = Long.parseLong(citaData.get("idMedico").toString());
            LocalDate fecha = LocalDate.parse(citaData.get("fecha").toString());
            LocalTime hora = LocalTime.parse(citaData.get("hora").toString(), DateTimeFormatter.ofPattern("HH:mm"));

            String telefonoFormulario = citaData.get("telefono") != null ? citaData.get("telefono").toString() : null;

            Map<String, Object> response = citaService.programarCita(idPaciente, idMedico, fecha, hora,telefonoFormulario);

            boolean success = (boolean) response.get("success");

            return success
                    ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Error en los datos enviados: " + e.getMessage()));
        }
    }

    // =========================================================
    // 4️⃣ MODIFICAR CITA (REPROGRAMAR)
    // =========================================================
    @PutMapping("/modificar/{idCita}")
    public ResponseEntity<?> modificarCita(
            @PathVariable Long idCita,
            @RequestBody Map<String, Object> datos) {

        try {
            LocalDate nuevaFecha = LocalDate.parse(datos.get("fecha").toString());
            LocalTime nuevaHora = LocalTime.parse(datos.get("hora").toString());

            Map<String, Object> response = citaService.modificarCita(idCita, nuevaFecha, nuevaHora);

            boolean success = (boolean) response.get("success");

            return success
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Error al modificar la cita: " + e.getMessage()));
        }
    }

    // =========================================================
    // 5️⃣ CANCELAR CITA
    // =========================================================
    @DeleteMapping("/cancelar/{idCita}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long idCita) {
        try {
            Map<String, Object> response = citaService.cancelarCita(idCita);

            boolean success = (boolean) response.get("success");

            return success
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al cancelar la cita: " + e.getMessage()));
        }
    }

    // =========================================================
    // 6️⃣ CITAS DEL PACIENTE
    // =========================================================
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> obtenerCitasPaciente(@PathVariable Long idPaciente) {
        try {
            List<Map<String, Object>> resultado = citaService.obtenerCitasPaciente(idPaciente);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al obtener citas del paciente"));
        }
    }



    // =========================================================
    // 7️⃣ CITAS FUTURAS DEL MÉDICO
    // =========================================================
    @GetMapping("/medico/{idMedico}/todas")
    public ResponseEntity<?> obtenerTodasCitasMedico(@PathVariable Long idMedico) {
        try {
            return ResponseEntity.ok(citaService.obtenerTodasCitasMedico(idMedico));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al obtener citas del médico"));
        }
    }

    @GetMapping("/{idCita}")
    public ResponseEntity<?> obtenerDetalleCita(@PathVariable Long idCita) {
        try {
            return ResponseEntity.ok(citaService.obtenerDetalleCita(idCita));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Cita no encontrada"));
        }
    }

    @PutMapping("/{idCita}/finalizar")
    public ResponseEntity<?> finalizarCita(
            @PathVariable Long idCita,
            @RequestBody Map<String, String> datos) {

        try {
            String observaciones = datos.get("observaciones");
            Map<String, Object> response = citaService.finalizarCita(idCita, observaciones);

            boolean success = (boolean) response.get("success");

            return success
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al finalizar la cita: " + e.getMessage()));
        }
    }
}
