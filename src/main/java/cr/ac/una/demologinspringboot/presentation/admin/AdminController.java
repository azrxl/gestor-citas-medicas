package cr.ac.una.demologinspringboot.presentation.admin;

import cr.ac.una.demologinspringboot.dto.admin.AdminPendingResponseDTO;
import cr.ac.una.demologinspringboot.dto.admin.MedicoApprovalDTO;
import cr.ac.una.demologinspringboot.dto.medico.MedicoPublicoDTO;
import cr.ac.una.demologinspringboot.dto.medico.MedicoPendienteDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/medicos")
    public ResponseEntity<AdminPendingResponseDTO> getMedicosPendientes(Authentication authentication) {
        String username = authentication.getName();
        List<MedicoApprovalDTO> dto = usuarioService.getMedicosForAdmin();

        AdminPendingResponseDTO response = new AdminPendingResponseDTO();
        response.setUsername(username);
        response.setMedicos(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Aprueba un médico específico por su ID.
     */
    @PostMapping("/medicos/aprobar/{id}")
    public ResponseEntity<MedicoPublicoDTO> aprobarMedico(@PathVariable Long id) {
        usuarioService.aprobarMedico(id);
        return ResponseEntity.noContent().build();
    }
}