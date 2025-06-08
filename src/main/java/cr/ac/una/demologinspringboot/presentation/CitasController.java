package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/cita")
@Controller
public class CitasController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;

    public CitasController(UsuarioService usuarioService, CitaService citaService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping("/agendar/{citaId}")
    public String agendar(@PathVariable Long citaId, HttpSession session, Model model) {
        Optional<Cita> cita = citaService.findCitaById(citaId);
        Usuario medico = new Usuario();
        if (cita.isPresent()) {
            medico = usuarioService.findByLogin(cita.get().getLoginMedico());
        }

        model.addAttribute("cita", cita);
        model.addAttribute("medico", medico);

        return "agendar";
    }
    @GetMapping("/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        citaService.reservarCita(id, username);
        return "redirect:/perfil";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {
        return "redirect:/home";
    }
}
