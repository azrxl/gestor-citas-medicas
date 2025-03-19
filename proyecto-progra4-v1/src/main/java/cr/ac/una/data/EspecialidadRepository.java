package cr.ac.una.data;

import cr.ac.una.logic.entities.Especialidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends CrudRepository<Especialidad, String> {

}
