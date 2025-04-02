package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.data.RolRepository;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.UsuarioService;
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
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public UsuarioController(PasswordEncoder passwordEncoder,UsuarioService usuarioService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
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

        usuarioService.registrarUsuario(usuario);

        return "redirect:/login";  // Redirigir al login después de registrarse
    }

    @GetMapping({"/", "/home"})
    public String home(HttpSession session, Model model) {
        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Guardar el usuario en sesión si aún no está
        if (session.getAttribute("username") == null) {
            session.setAttribute("username", username);
        }

        // Recuperar los criterios de búsqueda almacenados en la sesión (si existen)
        String especialidad = (String) session.getAttribute("especialidad");
        String ciudad = (String) session.getAttribute("ciudad");

        // Definir la lista de médicos según los filtros almacenados
        List<Usuario> medicos;
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            medicos = usuarioService.findByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            medicos = usuarioService.findByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            medicos = usuarioService.findByRolAndLocalidad("MEDICO", ciudad);
        } else {
            medicos = usuarioService.findByRol("MEDICO");
        }

        // Para poblar los select del formulario, obtenemos las especialidades y ciudades de la base de datos
        List<String> especialidades = usuarioService.findDistinctEspecialidad();
        List<String> ciudades = usuarioService.findDistinctLocalidades();

        // Agregar atributos al modelo
        model.addAttribute("username", username);
        model.addAttribute("medicos", medicos);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("filtroEspecialidad", especialidad);
        model.addAttribute("filtroCiudad", ciudad);

        return "home";
    }


    @GetMapping("/buscar")
    public String buscarMedicos(
            @RequestParam(name="especialidad", required = false) String especialidad,
            @RequestParam(name="ciudad", required = false) String ciudad,
            HttpSession session,
            Model model) {

        // Guardar en la sesión los filtros ingresados
        if (especialidad != null) {
            session.setAttribute("especialidad", especialidad);
        } else {
            especialidad = (String) session.getAttribute("especialidad");
        }

        if (ciudad != null) {
            session.setAttribute("ciudad", ciudad);
        } else {
            ciudad = (String) session.getAttribute("ciudad");
        }

        // Filtrar médicos según los criterios seleccionados
        List<Usuario> medicos;
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            medicos = usuarioService.findByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            medicos = usuarioService.findByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            medicos = usuarioService.findByRolAndLocalidad("MEDICO", ciudad);
        } else {
            medicos = usuarioService.findByRol("MEDICO");
        }

        // Obtener especialidades y ciudades únicas
        List<String> especialidades = usuarioService.findDistinctEspecialidad();
        List<String> ciudades = usuarioService.findDistinctLocalidades();

        // Agregar atributos al modelo
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("medicos", medicos);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("filtroEspecialidad", especialidad);
        model.addAttribute("filtroCiudad", ciudad);

        return "home"; // Redirige a la misma vista con los filtros aplicados
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login?logout";
    }
}
