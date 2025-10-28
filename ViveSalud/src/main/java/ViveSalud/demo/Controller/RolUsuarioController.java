package ViveSalud.demo.Controller;


import ViveSalud.demo.Model.RolUsuario;
import ViveSalud.demo.Services.RolUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/roles")
public class RolUsuarioController {

    @Autowired
    private RolUsuarioService rolUsuarioService;

    @GetMapping
    public List<RolUsuario> listarRoles() {
        return rolUsuarioService.listarRoles();
    }

    @PostMapping("/crear")
    public RolUsuario crearRol(@RequestBody RolUsuario rol) {
        return rolUsuarioService.guardarRol(rol);
    }
}
