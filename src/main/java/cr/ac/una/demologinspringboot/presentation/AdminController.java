package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.dto.entities.MedicoDTO;
import cr.ac.una.demologinspringboot.dto.entities.MedicoPendienteDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
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

    /**
     * @return una lista de todos los médicos pendientes de aprobación.
     */
    @GetMapping("/medicos/pendientes")
    public ResponseEntity<List<MedicoPendienteDTO>> getMedicosPendientes() {
        List<Usuario> medicosNoAprobados = usuarioService.findMedicosNoAprobados();
        List<MedicoPendienteDTO> medicosDTOs = new ArrayList<>();
        for (Usuario usuario : medicosNoAprobados) {
            medicosDTOs.add(new MedicoPendienteDTO(usuario));
        }
        return ResponseEntity.ok(medicosDTOs);
    }

    /**
     * Aprueba un médico específico por su ID.
     * @return el perfil completo del médico recién aprobado.
     */
    @PostMapping("/medicos/aprobar/{id}")
    public ResponseEntity<MedicoDTO> aprobarMedico(@PathVariable Long id) {
        Usuario medicoAprobado = usuarioService.aprobarMedico(id);

        return ResponseEntity.ok(new MedicoDTO(medicoAprobado));
    }
}