package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository  extends JpaRepository<Inscripcion,Long> {
}
