package ViveSalud.demo.Controller;

import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite conexión desde frontend
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {

        Usuario guardado = usuarioService.registrarUsuario(usuario);

        if (guardado == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario");
        }

        return ResponseEntity.ok(guardado);
    }


    // ✔ LOGIN CORREGIDO - ahora usa correo + contraseña
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datosLogin, HttpSession session) {

        String correo = datosLogin.get("correo");
        String contrasena = datosLogin.get("contrasena");

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest().body("Falta correo o contraseña");
        }

        Usuario usuario = usuarioService.iniciarSesionPorCorreo(correo, contrasena);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }

        // 🔥 GUARDAR USUARIO EN LA SESIÓN 🔥
        session.setAttribute("idUsuario", usuario.getIdUsuario());

        // ✔ Crear respuesta segura con todos los datos necesarios
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getIdUsuario());
        respuesta.put("nombres", usuario.getNombres());
        respuesta.put("apellidos", usuario.getApellidos());
        respuesta.put("dni", usuario.getDni() != null ? usuario.getDni() : "");
        respuesta.put("correo", usuario.getCorreo());
        respuesta.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        respuesta.put("direccion", usuario.getDireccion() != null ? usuario.getDireccion() : "");
        respuesta.put("fechaNacimiento", usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : "");
        respuesta.put("rol", usuario.getRolUsuario().getNombreRol());

        return ResponseEntity.ok(respuesta);
    }

    // ============================================================
//   OBTENER USUARIO POR ID
// ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Usuario no encontrado"));
            }

            // Crear respuesta completa con todos los datos
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("id", usuario.getIdUsuario());
            respuesta.put("idUsuario", usuario.getIdUsuario());
            respuesta.put("nombres", usuario.getNombres());
            respuesta.put("apellidos", usuario.getApellidos());
            respuesta.put("dni", usuario.getDni() != null ? usuario.getDni() : "");
            respuesta.put("correo", usuario.getCorreo());
            respuesta.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
            respuesta.put("direccion", usuario.getDireccion() != null ? usuario.getDireccion() : "");
            respuesta.put("fechaNacimiento", usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : "");
            respuesta.put("especialidad", usuario.getEspecialidad() != null ? usuario.getEspecialidad() : "");
            respuesta.put("nroColegiatura", usuario.getNroColegiatura() != null ? usuario.getNroColegiatura() : "");
            respuesta.put("rol", usuario.getRolUsuario().getNombreRol());

            // Información del rol
            Map<String, Object> rolInfo = new HashMap<>();
            rolInfo.put("idRol", usuario.getRolUsuario().getId_rol());
            rolInfo.put("nombreRol", usuario.getRolUsuario().getNombreRol());
            respuesta.put("rolUsuario", rolInfo);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuarioPorId(@PathVariable Long id, @RequestBody Map<String, Object> datosActualizados) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Usuario no encontrado"));
            }

            // Actualizar solo los campos que vienen en el request
            if (datosActualizados.containsKey("nombres")) {
                usuario.setNombres((String) datosActualizados.get("nombres"));
            }
            if (datosActualizados.containsKey("apellidos")) {
                usuario.setApellidos((String) datosActualizados.get("apellidos"));
            }
            if (datosActualizados.containsKey("correo")) {
                usuario.setCorreo((String) datosActualizados.get("correo"));
            }
            if (datosActualizados.containsKey("telefono")) {
                usuario.setTelefono((String) datosActualizados.get("telefono"));
            }
            if (datosActualizados.containsKey("direccion")) {
                usuario.setDireccion((String) datosActualizados.get("direccion"));
            }
            if (datosActualizados.containsKey("especialidad")) {
                usuario.setEspecialidad((String) datosActualizados.get("especialidad"));
            }

            Usuario actualizado = usuarioService.guardarUsuario(usuario);

            // Devolver respuesta completa
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Usuario actualizado exitosamente");
            respuesta.put("id", actualizado.getIdUsuario());
            respuesta.put("idUsuario", actualizado.getIdUsuario());
            respuesta.put("nombres", actualizado.getNombres());
            respuesta.put("apellidos", actualizado.getApellidos());
            respuesta.put("dni", actualizado.getDni());
            respuesta.put("correo", actualizado.getCorreo());
            respuesta.put("telefono", actualizado.getTelefono());
            respuesta.put("direccion", actualizado.getDireccion());
            respuesta.put("fechaNacimiento", actualizado.getFechaNacimiento() != null ? actualizado.getFechaNacimiento().toString() : "");
            respuesta.put("especialidad", actualizado.getEspecialidad() != null ? actualizado.getEspecialidad() : "");
            respuesta.put("nroColegiatura", actualizado.getNroColegiatura() != null ? actualizado.getNroColegiatura() : "");
            respuesta.put("rol", actualizado.getRolUsuario().getNombreRol());

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

}