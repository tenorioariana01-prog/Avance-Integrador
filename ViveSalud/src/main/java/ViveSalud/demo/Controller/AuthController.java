package ViveSalud.demo.Controller;


import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping ("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint de inicio de sesión
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Usuario loginRequest) {
        Map<String, Object> response = new HashMap<>();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombresAndContrasena(
                loginRequest.getNombres().trim(),
                loginRequest.getContrasena().trim()
        );

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            response.put("success", true);
            response.put("message", "Inicio de sesión exitoso.");
            response.put("usuario", usuario);
        } else {
            response.put("success", false);
            response.put("message", "Credenciales incorrectas o usuario no encontrado.");
        }

        return response;
    }

}
