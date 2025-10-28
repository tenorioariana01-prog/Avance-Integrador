package ViveSalud.demo.Services;


import ViveSalud.demo.Model.RolUsuario;
import ViveSalud.demo.Model.Usuario;
import ViveSalud.demo.Repository.RolUsuarioRepository;
import ViveSalud.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DniService dniService;

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        try {
            System.out.println("➡ Registrando usuario: " + usuario);

            // 🟢 Obtener nombres y apellidos desde la API del DNI
            if (usuario.getDni() != null && !usuario.getDni().isEmpty()) {
                Map<String, Object> datos = dniService.buscarPorDni(usuario.getDni());
                if (datos != null && datos.containsKey("nombres")) {
                    usuario.setNombres((String) datos.get("nombres"));
                    usuario.setApellidos((String) datos.get("apellidos"));
                }
            }

            // 🟢 Verificar y asignar rol
            RolUsuario rolAsignado = null;
            if (usuario.getRolUsuario() != null && usuario.getRolUsuario().getNombreRol() != null) {
                rolAsignado = rolUsuarioRepository.findByNombreRol(usuario.getRolUsuario().getNombreRol());
                if (rolAsignado == null) {
                    throw new IllegalArgumentException("❌ El rol especificado no existe en la base de datos.");
                }
            } else {
                rolAsignado = rolUsuarioRepository.findByNombreRol("PACIENTE");
                if (rolAsignado == null) {
                    throw new IllegalStateException("⚠️ El rol 'PACIENTE' no existe en la base de datos.");
                }
            }
            usuario.setRolUsuario(rolAsignado);

            // 🟢 Validar y limpiar campos según rol
            switch (rolAsignado.getNombreRol().toUpperCase()) {
                case "MEDICO":
                    if (usuario.getNroColegiatura() == null || usuario.getEspecialidad() == null) {
                        throw new IllegalArgumentException("Los campos nroColegiatura y especialidad son obligatorios para médicos.");
                    }
                    break;
                case "ADMIN":
                    // Limpiar campos de médico si los hubiera
                    usuario.setNroColegiatura(null);
                    usuario.setEspecialidad(null);
                    break;
                case "PACIENTE":
                    // Limpiar campos que no aplican
                    usuario.setNroColegiatura(null);
                    usuario.setEspecialidad(null);
                    break;
            }

            // 🟢 Guardar usuario
            Usuario guardado = usuarioRepository.save(usuario);
            System.out.println("✅ Usuario guardado correctamente: " + guardado);
            return guardado;

        } catch (Exception e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Usuario iniciarSesion(String nombres, String contrasena) {
        if (nombres == null || contrasena == null) return null;

        // Busca el usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombresAndContrasena(nombres, contrasena);

        // Devuelve el usuario si existe, sino null
        return usuarioOpt.orElse(null);
    }

    public Usuario actualizarUsuario(Long idUsuario, Usuario datosActualizados) {
        try {
            Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

            // 🟢 Actualizar solo campos permitidos
            if (datosActualizados.getCorreo() != null)
                usuarioExistente.setCorreo(datosActualizados.getCorreo());

            if (datosActualizados.getContrasena() != null)
                usuarioExistente.setContrasena(datosActualizados.getContrasena());

            if (datosActualizados.getTelefono() != null)
                usuarioExistente.setTelefono(datosActualizados.getTelefono());

            if (datosActualizados.getDireccion() != null)
                usuarioExistente.setDireccion(datosActualizados.getDireccion());

            // Si se permite actualizar especialidad o colegiatura solo si es médico
            if (usuarioExistente.getRolUsuario().getNombreRol().equalsIgnoreCase("MEDICO")) {
                if (datosActualizados.getEspecialidad() != null)
                    usuarioExistente.setEspecialidad(datosActualizados.getEspecialidad());
                if (datosActualizados.getNroColegiatura() != null)
                    usuarioExistente.setNroColegiatura(datosActualizados.getNroColegiatura());
            }

            Usuario actualizado = usuarioRepository.save(usuarioExistente);
            System.out.println("✅ Usuario actualizado correctamente: " + actualizado);
            return actualizado;

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            return null;
        }
    }

}
