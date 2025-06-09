package cr.ac.una.demologinspringboot.presentation.medicos;

import cr.ac.una.demologinspringboot.dto.medico.MedicoPublicoDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicosController {
    private final UsuarioService usuarioService;

    public MedicosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<MedicoPublicoDTO>> buscarMedicos(
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) String ciudad) {

        List<Usuario> medicos = usuarioService.findActiveAndApprovedMedicos(especialidad, ciudad);
        List<MedicoPublicoDTO> dtos = medicos.stream()
                .map(MedicoPublicoDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoPublicoDTO> getMedicoProfile(@PathVariable Long id) {
        Usuario medico = usuarioService.findUsuarioByIdOrThrow(id);
        return ResponseEntity.ok(new MedicoPublicoDTO(medico));
    }
}
