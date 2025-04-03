package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpSession;
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

    private final Service service;

    public AdminController(Service service) {
        this.service = service;
    }

    @GetMapping("/home")
    public String mostrarMedicosNoAprobados(HttpSession session, Model model) {
        List<Usuario> medicosNoAprobados = service.findMedicosNoAprobados();
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("medicos", medicosNoAprobados);
        return "admin/home";
    }

    @PostMapping("/aprobar/{id}")
    public String aprobarMedico(@PathVariable("id") Long id) {
        service.aprobarMedico(id);
        return "redirect:/admin/home";
    }
}