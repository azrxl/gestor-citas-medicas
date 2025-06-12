package cr.ac.una.demologinspringboot.presentation.medicos;

import cr.ac.una.demologinspringboot.dto.medico.MedicoPublicoDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "null", "http://localhost:4200", "http://localhost:5173" }, methods = { RequestMethod.POST, RequestMethod.GET })
@RequestMapping("/api/medicos")
public class MedicosController {
    private final UsuarioService usuarioService;
    private final CitaService citaService;

    public MedicosController(UsuarioService usuarioService, CitaService citaService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<MedicoPublicoDTO>> buscarMedicos(
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) String ciudad) {

        List<Usuario> medicos = usuarioService.findActiveAndApprovedMedicosWithCitas(especialidad, ciudad);
        List<MedicoPublicoDTO> medicoDtos = medicos.stream()
                .map(MedicoPublicoDTO::new)
                .toList();

        return ResponseEntity.ok(medicoDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoPublicoDTO> getMedicoProfile(@PathVariable Long id) {
        Usuario medico = usuarioService.findUsuarioByIdOrThrow(id);
        return ResponseEntity.ok(new MedicoPublicoDTO(medico));
    }


    @GetMapping("/citas/activas")
    public ResponseEntity<Map<Long, List<Cita>>> getCitasByMedicosActivos() {
        Map<Long, List<Cita>> medicosConCitas = new HashMap<>();

        List<Usuario> medicosActivos = usuarioService.findActiveAndApprovedMedicosWithCitas("", "");

        for (Usuario medico : medicosActivos) {
            List<Cita> citas = citaService.findCitasActivasByMedico(medico.getLogin());

            if (citas != null && !citas.isEmpty()) {
                medicosConCitas.put(medico.getId(), citas);
            }
        }

        return ResponseEntity.ok(medicosConCitas);
    }
}
