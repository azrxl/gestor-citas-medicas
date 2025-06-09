package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.dto.entities.MedicoDTO;
import cr.ac.una.demologinspringboot.dto.entities.MedicoUpdateRequestDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.SchedulerService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medicos")
public class MedicoController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;
    private final SchedulerService schedulerService;

    public MedicoController(UsuarioService usuarioService, CitaService citaService, SchedulerService schedulerService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
        this.schedulerService = schedulerService;
    }

    @PutMapping("/completar/perfil")
    public ResponseEntity<MedicoDTO> completarPerfil(@RequestBody @Valid MedicoUpdateRequestDTO requestDTO, Authentication authentication) {
        String horarioSemanal = schedulerService.scheduleParser(requestDTO);
        String medicoLogin = authentication.getName();
        Usuario medicoActualizado = usuarioService.completarMedico(medicoLogin, requestDTO, horarioSemanal);

        return null;
    }

    @GetMapping("/atender/{id}")
    public String atenderCita(@PathVariable Long id) {
        citaService.completarCita(id);
        return "redirect:/perfil";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {
        citaService.cancelarCitaPorMedico(id);
        return "redirect:/perfil";
    }
}