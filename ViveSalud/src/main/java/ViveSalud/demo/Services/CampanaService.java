package ViveSalud.demo.Services;

import ViveSalud.demo.Model.Campana;
import ViveSalud.demo.Repository.CampanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // ← FALTABA ESTE IMPORT

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    // Listar todas las campañas
    public List<Campana> listarCampanas() {
        return campanaRepository.findAll();
    }

    // Obtener campaña por ID
    public Campana obtenerCampanaPorId(Long id) {
        return campanaRepository.findById(id).orElse(null);
    }

    // Crear nueva campaña
    public Campana crearCampana(Campana campana) {
        return campanaRepository.save(campana);
    }

    // Actualizar campaña
    public Campana actualizarCampana(Long id, Campana campanaActualizada) {
        Optional<Campana> campanaExistente = campanaRepository.findById(id);

        if (campanaExistente.isPresent()) {
            Campana campana = campanaExistente.get();
            campana.setNombre(campanaActualizada.getNombre());
            campana.setFechaInicio(campanaActualizada.getFechaInicio());
            campana.setFechaFin(campanaActualizada.getFechaFin());
            campana.setLugar(campanaActualizada.getLugar());
            campana.setDescripcion(campanaActualizada.getDescripcion());
            campana.setIdResponsable(campanaActualizada.getIdResponsable());
            campana.setImagen(campanaActualizada.getImagen());
            campana.setDetalle(campanaActualizada.getDetalle());

            return campanaRepository.save(campana);
        }

        return null;
    }

    // Eliminar campaña
    public boolean eliminarCampana(Long id) {
        Optional<Campana> campana = campanaRepository.findById(id);
        if (campana.isPresent()) {
            campanaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
