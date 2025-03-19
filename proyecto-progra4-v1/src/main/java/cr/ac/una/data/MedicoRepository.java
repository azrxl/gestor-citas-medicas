package cr.ac.una.data;

import cr.ac.una.logic.entities.Medico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends CrudRepository<Medico, String> {

}
