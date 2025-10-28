package ViveSalud.demo.Services;


import ViveSalud.demo.Model.RolUsuario;
import ViveSalud.demo.Repository.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolUsuarioService {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    public List<RolUsuario> listarRoles() {
        return rolUsuarioRepository.findAll();
    }

    public RolUsuario guardarRol(RolUsuario rol) {
        return rolUsuarioRepository.save(rol);
    }
}
