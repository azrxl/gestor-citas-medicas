package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/home")
    public String mostrarMedicosNoAprobados(Authentication authentication, Model model) {
        List<Usuario> medicosNoAprobados = usuarioService.findMedicosNoAprobados();
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        model.addAttribute("medicos", medicosNoAprobados);
        return "admin/home";
    }

    @PostMapping("/aprobar/{id}")
    public String aprobarMedico(@PathVariable("id") Long id) {
        usuarioService.aprobarMedico(id);
        return "redirect:/admin/home";
    }
}