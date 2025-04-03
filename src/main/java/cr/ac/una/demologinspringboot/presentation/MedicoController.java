package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final Service service;

    public MedicoController(Service service) {
        this.service = service;
    }

    @GetMapping("/completar")
    public String perfil(HttpSession session, Model model) {
        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Buscar el usuario (médico) en la base de datos
        Usuario medico = service.findByLogin(username);

        // Agregarlo al modelo para pre-poblar el formulario
        model.addAttribute("usuario", medico);
        List<String> diasSemana = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        model.addAttribute("diasSemana", diasSemana);
        return "medico/completar"; // Devuelve la vista completar.html
    }



    @PostMapping("completar")
    public String actualizarPerfil(@Valid Usuario usuario, BindingResult result, Model model) {
        Usuario usuarioLogeado = service.findByLogin(usuario.getLogin());

        usuarioLogeado.setEspecialidad(usuario.getEspecialidad());
        usuarioLogeado.setCostoConsulta(usuario.getCostoConsulta());
        usuarioLogeado.setLocalidad(usuario.getLocalidad());
        usuarioLogeado.setHorarioSemanal(usuario.getHorarioSemanal());
        usuarioLogeado.setFrecuenciaCita(usuario.getFrecuenciaCita());
        service.actualizarUsuario(usuarioLogeado);

        return "redirect:/medico/citas";
    }

    @GetMapping("/citas")
    public String citas(HttpSession session, Model model) {
        return "/medico/citas";
    }

}