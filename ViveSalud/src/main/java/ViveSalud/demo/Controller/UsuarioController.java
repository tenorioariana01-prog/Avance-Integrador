package ViveSalud.demo.Controller;


import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Repository.UsuarioRepository;
import ViveSalud.demo.Services.DniService;
import ViveSalud.demo.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DniService dniService;

    @PostMapping("/registrar")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    // 🔹 Login de usuario (nombre + contraseña)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datosLogin) {
        try {
            String nombres = datosLogin.get("nombres");
            String contrasena = datosLogin.get("contrasena");

            if (nombres == null || contrasena == null) {
                return ResponseEntity.badRequest().body("Faltan nombres o contraseña");
            }

            Usuario usuario = usuarioService.iniciarSesion(nombres, contrasena);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario o contraseña incorrectos");
            }

            // Evitar problemas de serialización con la relación ManyToOne
            usuario.setRolUsuario(null);

            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }



    @PutMapping("/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
    }




}
