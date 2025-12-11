package ViveSalud.demo.Services;

import ViveSalud.demo.Model.Inscripcion;
import ViveSalud.demo.Repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    public Inscripcion registrarInscripcion(Long idUsuario, Long idCampana) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdUsuario(idUsuario);
        inscripcion.setIdCampana(idCampana);
        return inscripcionRepository.save(inscripcion);
    }

}
