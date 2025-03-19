package cr.ac.una.data;

import cr.ac.una.logic.entities.Localidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends CrudRepository<Localidad, String> {

}
