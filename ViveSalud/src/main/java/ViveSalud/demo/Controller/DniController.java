package ViveSalud.demo.Controller;


import ViveSalud.demo.Services.DniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dni")
public class DniController {
    @Autowired
    private DniService dniService;

    // 🔹 Endpoint para probar la API de DNI
    @GetMapping("/{numero}")
    public Map<String, Object> obtenerDatosPorDni(@PathVariable String numero) {
        return dniService.buscarPorDni(numero);
    }
}
