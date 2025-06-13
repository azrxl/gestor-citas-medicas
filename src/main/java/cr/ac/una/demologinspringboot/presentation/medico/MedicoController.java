package cr.ac.una.demologinspringboot.presentation.medico;

import cr.ac.una.demologinspringboot.dto.entities.CitaDTO;
import cr.ac.una.demologinspringboot.dto.medico.MedicoProfileDTO;
import cr.ac.una.demologinspringboot.dto.medico.MedicoPublicoDTO;
import cr.ac.una.demologinspringboot.dto.entities.UsuarioDTO;
import cr.ac.una.demologinspringboot.dto.medico.MedicoUpdateRequestDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.schedule.ScheduleParser;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.SchedulerService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/medico")
public class MedicoController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;
    private final ScheduleParser scheduleParser;

    public MedicoController(UsuarioService usuarioService, CitaService citaService, SchedulerService schedulerService, ScheduleParser scheduleParser) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
        this.scheduleParser = scheduleParser;
    }

    @GetMapping("/me/completar")
    public ResponseEntity<MedicoProfileDTO> verPerfil(Authentication authentication) {
        Usuario medico = usuarioService.findUsuarioByLoginOrThrow(authentication.getName());

        if (!"MEDICO".equals(medico.getRol())) {
            throw new IllegalArgumentException("Este endpoint solo es para médicos.");
        }

        MedicoProfileDTO dto = new MedicoProfileDTO();
        dto.setId(medico.getId());
        dto.setCedula(medico.getCedula());
        dto.setNombre(medico.getNombre());
        dto.setApellido(medico.getApellido());
        dto.setLogin(medico.getLogin());
        dto.setEspecialidad(medico.getEspecialidad());
        dto.setCostoConsulta(medico.getCostoConsulta());
        dto.setLocalidad(medico.getLocalidad());
        dto.setFrecuenciaCita(medico.getFrecuenciaCita());
        dto.setHorario(scheduleParser.deserialize(medico.getHorarioSemanal())); // Nueva función

        return ResponseEntity.ok(dto);
    }


    @PutMapping("/me/completar")
    public ResponseEntity<MedicoProfileDTO> completarPerfil(
            @RequestBody @Valid MedicoUpdateRequestDTO req,
            Authentication auth
    ) {
        String horarioSemanal = scheduleParser.parse(req.getHorario());
        Usuario mActualizado = usuarioService.completarMedico(
                auth.getName(), req, horarioSemanal
        );

        // Reconstruyo el DTO idéntico al GET /me/completar
        MedicoProfileDTO dto = new MedicoProfileDTO();
        dto.setId(mActualizado.getId());
        dto.setCedula(mActualizado.getCedula());
        dto.setNombre(mActualizado.getNombre());
        dto.setApellido(mActualizado.getApellido());
        dto.setLogin(mActualizado.getLogin());
        dto.setEspecialidad(mActualizado.getEspecialidad());
        dto.setCostoConsulta(mActualizado.getCostoConsulta());
        dto.setLocalidad(mActualizado.getLocalidad());
        dto.setFrecuenciaCita(mActualizado.getFrecuenciaCita());
        // Muy importante: vuelve a deserializar el string
        dto.setHorario(scheduleParser.deserialize(mActualizado.getHorarioSemanal()));

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/me/perfil")
    public ResponseEntity<MedicoPublicoDTO> getMyProfile(Authentication authentication) {
        Usuario usuario = usuarioService.findUsuarioByLoginOrThrow(authentication.getName());
        return ResponseEntity.ok(new MedicoPublicoDTO(usuario));
    }

    @GetMapping("/me/citas")
    public ResponseEntity<List<CitaDTO>> verCitas(Authentication authentication, @RequestParam(required = false) String estado) {
        String medicoLogin = authentication.getName();
        List<Cita> citasFiltradas = citaService.findCitasFiltradasParaMedico(medicoLogin, estado);
        List<CitaDTO> citasDto = citasFiltradas.stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(citasDto);
    }

    @GetMapping("/me/citas/historial")
    public ResponseEntity<List<CitaDTO>> getMyCitasHistory(
            Authentication authentication,
            @RequestParam(required = false) String estado) {

        Usuario usuario = usuarioService.findUsuarioByLoginOrThrow(authentication.getName());
        List<Cita> citas = citaService.findCitasParaUsuario(usuario.getLogin(), usuario.getRol(), estado);

        List<CitaDTO> dtos = citas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/citas/completar/{id}")
    public ResponseEntity<CitaDTO> completarCita(@PathVariable Long id, Authentication authentication) {
        Cita cita = citaService.findCitaByIdOrThrow(id);
        if (!cita.getLoginMedico().equals(authentication.getName())) {
            throw new AccessDeniedException("Carece de permiso para modificar esta cita.");
        }
        Cita citaCompletada = citaService.completarCita(id);
        return ResponseEntity.ok(this.convertToDto(citaCompletada));
    }

    @PostMapping("/citas/cancelar/{id}")
    public ResponseEntity<CitaDTO> cancelarCita(@PathVariable Long id, Authentication authentication) {
        Cita cita = citaService.findCitaByIdOrThrow(id);
        if (!cita.getLoginMedico().equals(authentication.getName())) {
            throw new AccessDeniedException("Carece de permiso para modificar esta cita.");
        }
        Cita citaCompletada = citaService.cancelarCitaPorMedico(id);
        return ResponseEntity.ok(this.convertToDto(citaCompletada));
    }

    private CitaDTO convertToDto(Cita cita) {
        Usuario medico = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginMedico());
        Usuario paciente = null;
        if (cita.getLoginPaciente() != null) {
            paciente = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginPaciente());
        }
        return new CitaDTO(cita, medico, paciente);
    }
}