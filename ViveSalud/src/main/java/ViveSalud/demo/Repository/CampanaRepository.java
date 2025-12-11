package ViveSalud.demo.Repository;

import ViveSalud.demo.Model.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampanaRepository extends JpaRepository<Campana,Long> {

    @Query("SELECT c FROM Campana c JOIN Inscripcion i ON c.id = i.idCampana WHERE i.idUsuario = :idUsuario")
    List<Campana> findCampanasByUsuario(@Param("idUsuario") Long idUsuario);

}
