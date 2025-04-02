package cr.ac.una.demologinspringboot.logic.service;

import cr.ac.una.demologinspringboot.data.RolRepository;
import cr.ac.una.demologinspringboot.logic.entities.Rol;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
    public List<Usuario> findByRolAndEspecialidad(String rol, String especialidad) {
        return usuarioRepository.findByRolAndEspecialidad(rol, especialidad);
    }
    public List<Usuario> findByRolAndLocalidad(String rol, String localidad) {
        return usuarioRepository.findByRolAndLocalidad(rol,localidad);
    }
    public List<Usuario> findByRolAndEspecialidadAndLocalidad(String rol, String especialidad, String localidad) {
        return usuarioRepository.findByRolAndEspecialidadAndLocalidad(rol, especialidad, localidad);
    }

    public List<String> findDistinctEspecialidad() {
        return usuarioRepository.findDistinctEspecialidad();
    }
    public List<String> findDistinctLocalidades() {
        return usuarioRepository.findDistinctLocalidades();
    }

    public void registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public List<Rol> rolFindAll() {
        return rolRepository.findAll();
    }
}
