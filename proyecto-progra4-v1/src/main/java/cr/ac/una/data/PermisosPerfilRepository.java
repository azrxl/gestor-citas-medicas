package cr.ac.una.data;

import cr.ac.una.logic.entities.Permisosperfil;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisosPerfilRepository extends CrudRepository<Permisosperfil, String> {

}
