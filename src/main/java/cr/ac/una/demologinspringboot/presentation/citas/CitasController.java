package cr.ac.una.demologinspringboot.presentation.citas;

import cr.ac.una.demologinspringboot.dto.entities.CitaDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
public class CitasController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;

    public CitasController(CitaService citaService, UsuarioService usuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<CitaDTO> verCita(@PathVariable Long id) {
        Cita cita = citaService.findCitaByIdOrThrow(id);
        return ResponseEntity.ok(convertCitaToDto(cita));
    }

    @PutMapping("/reservar/{id}")
    public ResponseEntity<CitaDTO> confirmarCita(@PathVariable Long id, Authentication authentication) {
        String pacienteLogin = authentication.getName();
        Cita cita = citaService.reservarCita(id, pacienteLogin);
        return ResponseEntity.ok(convertCitaToDto(cita));
    }

    private CitaDTO convertCitaToDto(Cita cita) {
        Usuario medico = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginMedico());
        Usuario paciente = null;
        if (cita.getLoginPaciente() != null) {
            paciente = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginPaciente());
        }
        return new CitaDTO(cita, medico, paciente);
    }
}

