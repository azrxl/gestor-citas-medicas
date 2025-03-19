package cr.ac.una.data;

import cr.ac.una.logic.entities.Permiso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends CrudRepository<Permiso, String> {

}
