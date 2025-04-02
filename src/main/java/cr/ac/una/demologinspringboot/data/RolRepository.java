package cr.ac.una.demologinspringboot.data;

import cr.ac.una.demologinspringboot.logic.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
