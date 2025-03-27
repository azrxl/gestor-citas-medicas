package cr.ac.una.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cr.ac.una.logic.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {

}
