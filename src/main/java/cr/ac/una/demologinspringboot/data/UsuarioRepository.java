package cr.ac.una.demologinspringboot.data;


import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByRolAndAprobado(String rol, boolean aprobado);
    List<Usuario> findByRolAndEspecialidad(String rol, String especialidad);
    List<Usuario> findByRolAndLocalidad(String rol, String localidad);
    List<Usuario> findByRolAndEspecialidadAndLocalidad(String rol, String especialidad, String localidad);
    @Query("SELECT DISTINCT u.especialidad FROM Usuario u")
    List<String> findDistinctEspecialidad();
    @Query("SELECT DISTINCT u.localidad FROM Usuario u")
    List<String> findDistinctLocalidades();
}
//hola rico