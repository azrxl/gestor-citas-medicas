package cr.ac.una.data;

import cr.ac.una.logic.entities.Perfil;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends CrudRepository<Perfil, String> {

}
