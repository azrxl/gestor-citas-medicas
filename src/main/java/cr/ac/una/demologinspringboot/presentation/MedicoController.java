package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.dto.MedicoDto;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.citas.SchedulerService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;
    private final SchedulerService schedulerService;
    private static final List<String> DIAS_SEMANA = List.of(
        "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"
    );

    public MedicoController(UsuarioService usuarioService, CitaService citaService, SchedulerService schedulerService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
        this.schedulerService = schedulerService;
    }

    @GetMapping("/completar")
    public String completarPerfil(Authentication authentication, Model model) {
        String username = authentication.getName();
        Usuario medico = usuarioService.findByLogin(username);

        model.addAttribute("usuario", medico);
        model.addAttribute("diasSemana", DIAS_SEMANA);
        return "medico/completar";
    }

    @PostMapping("/completar")
    public String completarPerfil(MedicoDto medicoDto, HttpServletRequest request, Authentication authentication) {
        StringBuilder scheduleBuilder = schedulerService.scheduleParser(request);
        String horarioSemanal = scheduleBuilder.toString();

        String medicoLogin = authentication.getName();
        usuarioService.completarMedico(medicoLogin, medicoDto, horarioSemanal);

        return "redirect:/home";
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