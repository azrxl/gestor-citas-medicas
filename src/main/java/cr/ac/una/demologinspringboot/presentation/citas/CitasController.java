package cr.ac.una.demologinspringboot.presentation.citas;

import cr.ac.una.demologinspringboot.dto.cita.ConfirmCitaResponseDTO;
import cr.ac.una.demologinspringboot.dto.entities.CitaDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.ReservarCitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = { "null", "http://localhost:4200", "http://localhost:5173" }, methods = { RequestMethod.POST, RequestMethod.GET })
public class CitasController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final ReservarCitaService reservarCitaService;

    public CitasController(CitaService citaService, UsuarioService usuarioService, ReservarCitaService reservarCitaService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.reservarCitaService = reservarCitaService;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ConfirmCitaResponseDTO> getCita(@PathVariable Long id) {
        ConfirmCitaResponseDTO dto = reservarCitaService.getConfirmCita(id);
        return ResponseEntity.ok(dto);
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

