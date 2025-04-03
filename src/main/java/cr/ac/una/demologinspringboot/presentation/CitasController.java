package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/cita")
@Controller
public class CitasController {

    private final Service service;

    public CitasController(Service service) {
        this.service = service;
    }

    @GetMapping("/agendar/{citaId}")
    public String agendar(@PathVariable Long citaId, HttpSession session, Model model) {
        // Buscar la cita por su ID
        Optional<Cita> cita = service.findCitaById(citaId);
        // Buscar al médico asociado a la cita
        Usuario medico = new Usuario();
        if (cita.isPresent()) {
            medico = service.findByLogin(cita.get().getLoginMedico());
        }

        model.addAttribute("cita", cita);
        model.addAttribute("medico", medico);

        return "agendar"; // La vista "agendar.html"
    }
    @GetMapping("/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuarioLogeado = service.findByLogin(session.getAttribute("username").toString());
        service.actualizarEstadoCita(id, "PENDIENTE", usuarioLogeado.getLogin()); // O el estado que corresponda
        return "redirect:/perfil"; // O a otra vista que confirme la acción
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {
        return "redirect:/home";
    }
}
