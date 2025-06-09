package cr.ac.una.demologinspringboot.presentation.perfil;

import cr.ac.una.demologinspringboot.dto.entities.CitaDTO;
import cr.ac.una.demologinspringboot.dto.entities.UsuarioDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/me")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;

    public PerfilController(UsuarioService usuarioService, CitaService citaService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> getMyProfile(Authentication authentication) {
        Usuario usuario = usuarioService.findUsuarioByLoginOrThrow(authentication.getName());
        return ResponseEntity.ok(new UsuarioDTO(usuario));
    }

    @GetMapping("/citas")
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

    private CitaDTO convertToDto(Cita cita) {
        Usuario medico = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginMedico());
        Usuario paciente = null;
        if (cita.getLoginPaciente() != null) {
            paciente = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginPaciente());
        }
        return new CitaDTO(cita, medico, paciente);
    }
}