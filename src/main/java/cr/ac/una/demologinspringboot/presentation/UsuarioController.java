package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;
    private final Service service;

    public UsuarioController(PasswordEncoder passwordEncoder, Service service) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return "registro";  // Si hay errores, volver al formulario
        }

        service.registrarUsuario(usuario);

        return "redirect:/login";  // Redirigir al login después de registrarse
    }

    @GetMapping({"/", "/home", "/buscar"})
    public String home(HttpSession session, Model model,
                       @RequestParam(name = "especialidad", required = false) String especialidad,
                       @RequestParam(name = "ciudad", required = false) String ciudad) {

        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Guardar el usuario en sesión si aún no está
        if (session.getAttribute("username") == null) {
            session.setAttribute("username", username);
        }

        // Guardar en la sesión los filtros ingresados
        guardarFiltrosEnSesion(session, especialidad, ciudad);

        // Renderizar la página home con los filtros
        renderizarHome(session, model);

        return "home";
    }

    private void guardarFiltrosEnSesion(HttpSession session, String especialidad, String ciudad) {
        session.setAttribute("especialidad", especialidad != null ? especialidad : session.getAttribute("especialidad"));
        session.setAttribute("ciudad", ciudad != null ? ciudad : session.getAttribute("ciudad"));
    }

    private void renderizarHome(HttpSession session, Model model) {
        String especialidad = (String) session.getAttribute("especialidad");
        String ciudad = (String) session.getAttribute("ciudad");

        List<Usuario> medicos = obtenerMedicosFiltrados(especialidad, ciudad);
        List<String> especialidades = service.findUsuarioDistinctEspecialidad();
        List<String> ciudades = service.findUsuarioDistinctLocalidades();

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("medicos", medicos);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("filtroEspecialidad", especialidad);
        model.addAttribute("filtroCiudad", ciudad);
    }

    private List<Usuario> obtenerMedicosFiltrados(String especialidad, String ciudad) {
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            return service.findUsuarioByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            return service.findUsuarioByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            return service.findUsuarioByRolAndLocalidad("MEDICO", ciudad);
        } else {
            return service.findUsuarioByRol("MEDICO");
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login?logout";
    }
}
