package cr.ac.una.demologinspringboot.presentation.perfil;

import cr.ac.una.demologinspringboot.dto.perfil.AppointmentBlockDTO;
import cr.ac.una.demologinspringboot.dto.perfil.PerfilResponseDTO;
import cr.ac.una.demologinspringboot.dto.perfil.UsuarioProfileDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/me")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;

    @Autowired
    public PerfilController(UsuarioService usuarioService, CitaService citaService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponseDTO> getPerfil(
            Authentication authentication
    ) {
        // 1. Cargo el usuario y lo mapeo a DTO
        String login = authentication.getName();
        Usuario usuario = usuarioService.findUsuarioByLoginOrThrow(login);
        UsuarioProfileDTO usuarioDto = usuarioService.toProfileDto(usuario);

        // 2. Obtengo el histórico de citas como bloques según rol
        List<AppointmentBlockDTO> blocks = citaService
                .getHistoricoCitasParaUsuarioComoBlocks(
                        usuario.getLogin(),
                        usuario.getRol()
                );

        // 3. Empaqueto todo en el response DTO
        PerfilResponseDTO resp = new PerfilResponseDTO();
        resp.setUsuario(usuarioDto);
        resp.setHistoricoCitas(blocks);

        return ResponseEntity.ok(resp);
    }
}
