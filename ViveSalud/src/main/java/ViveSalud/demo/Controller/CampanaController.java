package ViveSalud.demo.Controller;

import ViveSalud.demo.Model.Campana;
import ViveSalud.demo.Model.Inscripcion;
import ViveSalud.demo.Repository.CampanaRepository;
import ViveSalud.demo.Services.CampanaService;
import ViveSalud.demo.Services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/campanas")
public class CampanaController {

    @Autowired
    private CampanaService campanaService;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private InscripcionService inscripcionService;

    // LISTAR TODAS LAS CAMPAÑAS
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)  // ✅ AGREGAR ESTO
    public ResponseEntity<?> listarCampanas() {
        try {
            List<Campana> campanas = campanaService.listarCampanas();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "campanas", campanas
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al listar campañas"));
        }
    }

    @PostMapping("/{idCampaña}/inscribir")
    public ResponseEntity<?> inscribirUsuario(
            @PathVariable Long idCampaña,
            @RequestBody Map<String, Long> body) {

        Long idUsuario = body.get("idUsuario");
        Inscripcion inscripcion = inscripcionService.registrarInscripcion(idUsuario, idCampaña);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Usuario inscrito correctamente",
                "inscripcion", inscripcion
        ));
    }


    // OBTENER CAMPAÑA POR ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)  // ✅ AGREGAR ESTO
    public ResponseEntity<?> obtenerCampana(@PathVariable Long id) {
        try {
            Campana campana = campanaService.obtenerCampanaPorId(id);

            if (campana == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Campaña no encontrada"));
            }

            return ResponseEntity.ok(Map.of("success", true, "campana", campana));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al obtener campaña"));
        }
    }

    // CREAR CAMPAÑA
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearCampana(@RequestBody Campana campana) {
        try {
            Campana nuevaCampana = campanaService.crearCampana(campana);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "message", "Campaña creada exitosamente",
                            "campana", nuevaCampana
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al crear campaña: " + e.getMessage()));
        }
    }

    // ACTUALIZAR CAMPAÑA
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> actualizarCampana(
            @PathVariable Long id,
            @RequestBody Campana campanaActualizada) {

        try {
            Campana campana = campanaService.actualizarCampana(id, campanaActualizada);

            if (campana == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Campaña no encontrada"));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Campaña actualizada exitosamente",
                    "campana", campana
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error al actualizar campaña: " + e.getMessage()));
        }
    }

    // ELIMINAR CAMPAÑA
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> eliminarCampana(@PathVariable Long id) {
        try {
            boolean eliminada = campanaService.eliminarCampana(id);

            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Campaña no encontrada"));
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "Campaña eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al eliminar campaña: " + e.getMessage()));
        }
    }

    @GetMapping("/usuarios/{idUsuario}/campanas")
    public List<Campana> obtenerCampanasPorUsuario(@PathVariable Long idUsuario) {
        return campanaRepository.findCampanasByUsuario(idUsuario);
    }

}