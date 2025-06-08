package cr.ac.una.demologinspringboot.logic.service.usuario;

import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import cr.ac.una.demologinspringboot.dto.AppointmentBlock;
import cr.ac.una.demologinspringboot.dto.HomeViewModel;
import cr.ac.una.demologinspringboot.dto.MedicoDto;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.SchedulerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void registrarUsuario(Usuario usuario, String confirmPassword) {
        if (!usuario.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        usuario.setAprobado(!"Medico".equals(usuario.getRol()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public void aprobarMedico(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario medico = usuario.get();
            if ("MEDICO".equals(medico.getRol())) {
                medico.setAprobado(true);
                usuarioRepository.save(medico);
            }
        }
    }

    public void actualizarUsuario(@Valid Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public void completarMedico(String login, MedicoDto profileDto, String horarioSemanal) {
        Usuario medico = this.findByLogin(login);
        if (medico != null && "MEDICO".equals(medico.getRol())) {
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
        } else {
            throw new RuntimeException("Usuario no es un médico o no fue encontrado.");
        }
    }

    // En UsuarioService.java (necesitarás inyectar CitaService)

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

    private List<Usuario> findActiveAndApprovedMedicos(String especialidad, String ciudad) {
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
