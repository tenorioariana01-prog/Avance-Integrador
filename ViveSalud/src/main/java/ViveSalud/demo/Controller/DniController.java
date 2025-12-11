package ViveSalud.demo.Controller;


import ViveSalud.demo.Services.DniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dni")
@CrossOrigin(origins = "*")
public class DniController {
    @Autowired
    private DniService dniService;

    @GetMapping("/{numero}")
    public ResponseEntity<?> obtenerDatosPorDni(@PathVariable String numero) {

        // Validación básica
        if (numero.length() != 8 || !numero.matches("\\d+")) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "El DNI debe tener 8 dígitos numéricos")
            );
        }

        try {
            Map<String, Object> datos = dniService.buscarPorDni(numero);

            if (datos == null || datos.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("error", "No se encontraron datos para el DNI ingresado")
                );
            }

            return ResponseEntity.ok(datos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Ocurrió un error al consultar el DNI", "detalle", e.getMessage())
            );
        }
    }
}
