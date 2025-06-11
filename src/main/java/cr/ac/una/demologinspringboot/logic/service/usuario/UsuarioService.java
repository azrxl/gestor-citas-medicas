package cr.ac.una.demologinspringboot.logic.service.usuario;

import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import cr.ac.una.demologinspringboot.dto.medico.MedicoUpdateRequestDTO;
import cr.ac.una.demologinspringboot.dto.ui.AppointmentBlock;
import cr.ac.una.demologinspringboot.dto.ui.HomeViewModel;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.exceptions.DuplicateResourceException;
import cr.ac.una.demologinspringboot.logic.exceptions.ResourceNotFoundException;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SchedulerService schedulerService;
    private final CitaService citaService;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, SchedulerService schedulerService, CitaService citaService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.schedulerService = schedulerService;
        this.citaService = citaService;
    }

    public Usuario findUsuarioByIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado."));
    }

    public Usuario findUsuarioByLoginOrThrow(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con login '" + login + "' no encontrado."));
    }

    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
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
        return usuarioRepository.findByRolAndLocalidad(rol, localidad);
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

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
            throw new DuplicateResourceException (
                    "El nombre de usuario '" + usuario.getLogin() + "' o la cedula '" + usuario.getCedula() + "' ya está en uso."
            );
        }

        usuario.setAprobado(!"MEDICO".equals(usuario.getRol()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        try {
            usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException (
                    "El nombre de usuario '" + usuario.getLogin() + "' o la cedula '" + usuario.getCedula() + "' ya está en uso."
            );
        }
        return usuario;
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario aprobarMedico(Long id) {
        Usuario medico = findUsuarioByIdOrThrow(id);

        if (!"MEDICO".equals(medico.getRol())) {
            throw new IllegalArgumentException("El usuario con ID " + id + " no es un médico y no puede ser aprobado.");
        }

        medico.setAprobado(true);
        return usuarioRepository.save(medico);
    }

    public Usuario completarMedico(String login, MedicoUpdateRequestDTO profileDto, String horarioSemanal) {
        Usuario medico = this.findUsuarioByLoginOrThrow(login);

        if (!"MEDICO".equals(medico.getRol())) {
            throw new IllegalArgumentException("Solo los usuarios con rol MEDICO pueden completar su perfil.");
        }

        medico.setEspecialidad(profileDto.getEspecialidad());
        medico.setCostoConsulta(profileDto.getCostoConsulta());
        medico.setLocalidad(profileDto.getLocalidad());
        medico.setFrecuenciaCita(profileDto.getFrecuenciaCita());
        medico.setHorarioSemanal(horarioSemanal);

        this.actualizarUsuario(medico);
        schedulerService.generateAndSaveMonthlyCitas(
                medico.getHorarioSemanal(),
                medico.getFrecuenciaCita(),
                medico.getLogin()
        );
        return medico;
    }

    public HomeViewModel prepararHomeViewModel(String especialidad, String ciudad) {
        List<Usuario> medicos = this.findActiveAndApprovedMedicos(especialidad, ciudad);
        List<String> especialidades = this.findUsuarioDistinctEspecialidad();
        List<String> ciudades = this.findUsuarioDistinctLocalidades();

        Map<Long, List<AppointmentBlock>> citasPorMedico = new HashMap<>();
        for (Usuario medico : medicos) {
            List<Cita> citasActivas = citaService.findCitasActivasByMedico(medico.getLogin());
            Map<LocalDate, List<Cita>> citasPorFecha = citasActivas.stream()
                    .collect(Collectors.groupingBy(Cita::getFecha));
            List<AppointmentBlock> blocks = citasPorFecha.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .limit(3)
                    .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            if (!blocks.isEmpty()) {
                citasPorMedico.put(medico.getId(), blocks);
            }
        }

        List<Usuario> medicosConCitas = medicos.stream()
                .filter(m -> citasPorMedico.containsKey(m.getId()))
                .collect(Collectors.toList());

        HomeViewModel viewModel = new HomeViewModel();
        viewModel.setMedicos(medicosConCitas);
        viewModel.setEspecialidades(especialidades);
        viewModel.setCiudades(ciudades);
        viewModel.setCitasPorMedico(citasPorMedico);

        return viewModel;
    }

    public List<Usuario> findActiveAndApprovedMedicos(String especialidad, String ciudad) {
        List<Usuario> medicos;
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            medicos = this.findUsuarioByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            medicos = this.findUsuarioByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            medicos = this.findUsuarioByRolAndLocalidad("MEDICO", ciudad);
        } else {
            medicos = this.findUsuarioByRol("MEDICO");
        }

        return medicos.stream()
                .filter(Usuario::getAprobado)
                .filter(Usuario::isProfileComplete)
                .collect(Collectors.toList());
    }
}
