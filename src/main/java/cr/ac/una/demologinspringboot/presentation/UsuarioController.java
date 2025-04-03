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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    private final Service service;

    public UsuarioController(Service service) {
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

        // Aquí determinamos si el usuario es administrador.
        Usuario usuarioLogeado = service.findByLogin(username);
        if (usuarioLogeado != null) {
            if ("ADMIN".equals(usuarioLogeado.getRol())) {
                // Si es administrador, redirige a la vista de médicos NO aprobados
                return "redirect:/admin/home";
            } else if ("MEDICO".equals(usuarioLogeado.getRol())) {
                // Si es médico, verificamos si su perfil está completo
                if (perfilIncompleto(usuarioLogeado)) {
                    // Redirigir a la página de perfil para completar los datos
                    return "redirect:/medico/completar";
                }
            }
        }
        // Guardar en la sesión los filtros ingresados
        guardarFiltrosEnSesion(session, especialidad, ciudad);
        // Renderizar la página home con los filtros
        renderizarHome(session, model);
        return "home";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Usuario usuarioLogeado = service.findByLogin(session.getAttribute("username").toString());
        model.addAttribute("usuario", usuarioLogeado);
        return "perfil";
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
        List<Usuario> medicos;
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            medicos = service.findUsuarioByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            medicos = service.findUsuarioByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            medicos = service.findUsuarioByRolAndLocalidad("MEDICO", ciudad);
        } else {
            medicos = service.findUsuarioByRol("MEDICO");
        }

        return medicos.stream()
                .filter(Usuario::getAprobado)
                .collect(Collectors.toList());
    }

    private boolean perfilIncompleto(Usuario medico) {
        // Ejemplo de campos obligatorios para un médico: especialidad, costo de consulta, localidad, horario_semanal y frecuencia_cita
        return medico.getEspecialidad() == null || medico.getEspecialidad().trim().isEmpty()
                || medico.getCostoConsulta() == null
                || medico.getLocalidad() == null || medico.getLocalidad().trim().isEmpty()
                || medico.getHorarioSemanal() == null || medico.getHorarioSemanal().trim().isEmpty()
                || medico.getFrecuenciaCita() == null;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login?logout";
    }
}
