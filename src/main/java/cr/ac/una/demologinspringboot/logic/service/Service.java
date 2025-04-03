package cr.ac.una.demologinspringboot.logic.service;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.data.RolRepository;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Rol;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CitaRepository citaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public Service(UsuarioRepository usuarioRepository, RolRepository rolRepository, CitaRepository citaRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.citaRepository = citaRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Usuario> findUsuarioByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
    public List<Usuario> findMedicosNoAprobados() {
        return usuarioRepository.findByRolAndAprobado("MEDICO", false);
    }
    public List<Usuario> findUsuarioByRolAndEspecialidad(String rol, String especialidad) {
        return usuarioRepository.findByRolAndEspecialidad(rol, especialidad);
    }
    public List<Usuario> findUsuarioByRolAndLocalidad(String rol, String localidad) {
        return usuarioRepository.findByRolAndLocalidad(rol,localidad);
    }
    public List<Usuario> findUsuarioByRolAndEspecialidadAndLocalidad(String rol, String especialidad, String localidad) {
        return usuarioRepository.findByRolAndEspecialidadAndLocalidad(rol, especialidad, localidad);
    }

    public List<String> findUsuarioDistinctEspecialidad() {
        return usuarioRepository.findDistinctEspecialidad().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<String> findUsuarioDistinctLocalidades() {
        return usuarioRepository.findDistinctLocalidades().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void registrarUsuario(Usuario usuario) {
        usuario.setAprobado(!"MEDICO".equals(usuario.getRol()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public List<Rol> rolFindAll() {
        return rolRepository.findAll();
    }

    public List<Cita> citaFindAll() {
        return citaRepository.findAll();
    }

    public List<Cita> citaFindByLoginMedico(String medico) {
        return citaRepository.findByLoginMedico(medico);
    }
    public List<Cita> citaFindByLoginPaciente(String paciente) {
        return citaRepository.findByLoginPaciente(paciente);
    }

    public void aprobarMedico(Long id) {
        Optional<Usuario> op = usuarioRepository.findById(id);
        if (op.isPresent()) {
            Usuario medico = op.get();
            // Asegurarse de que es un MEDICO
            if ("MEDICO".equals(medico.getRol())) {
                medico.setAprobado(true);
                usuarioRepository.save(medico);
            }
        }
    }

    public void actualizarUsuario(@Valid Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
