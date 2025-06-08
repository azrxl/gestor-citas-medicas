package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.dto.AppointmentBlock;
import cr.ac.una.demologinspringboot.dto.HomeViewModel;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.citas.CitaService;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CitaService citaService;

    public UsuarioController(UsuarioService usuarioService, CitaService citaService) {
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid Usuario usuario, BindingResult result, Model model,
                                   @RequestParam("confirmPassword") String confirmPassword) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            usuarioService.registrarUsuario(usuario, confirmPassword);
        } catch (IllegalArgumentException e) {
            model.addAttribute("confirmPasswordError", e.getMessage());
            return "registro";
        }

        return "redirect:/login";
    }
    // En UsuarioController.java

    @GetMapping({"/", "/home", "/buscar"})
    public String home(Authentication authentication, Model model,
                       @RequestParam(name = "especialidad", required = false) String especialidad,
                       @RequestParam(name = "ciudad", required = false) String ciudad) {

        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuarioLogeado = usuarioService.findByLogin(authentication.getName());
            if (usuarioLogeado != null) {
                if ("ADMIN".equals(usuarioLogeado.getRol())) {
                    return "redirect:/admin/home";
                } else if ("MEDICO".equals(usuarioLogeado.getRol()) && !usuarioLogeado.isProfileComplete()) {
                    return "redirect:/medico/completar";
                }
            }
        }
        HomeViewModel viewModel = usuarioService.prepararHomeViewModel(especialidad, ciudad);

        model.addAttribute("medicos", viewModel.getMedicos());
        model.addAttribute("especialidades", viewModel.getEspecialidades());
        model.addAttribute("ciudades", viewModel.getCiudades());
        model.addAttribute("activeCitas", viewModel.getCitasPorMedico());
        model.addAttribute("filtroEspecialidad", especialidad);
        model.addAttribute("filtroCiudad", ciudad);
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        return "home";
    }

    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model,
                         @RequestParam(name = "estado", required = false) String estado,
                         @RequestParam(name = "paciente", required = false) String paciente,
                         @RequestParam(name = "doctor", required = false) String doctor) {

        String username = authentication.getName();
        Usuario usuarioLogeado = usuarioService.findByLogin(username);
        model.addAttribute("usuario", usuarioLogeado);

        List<AppointmentBlock> blocks = new ArrayList<>();

        if ("MEDICO".equals(usuarioLogeado.getRol())) {
            blocks = citaService.getHistoricoCitasMedicoComoBlocks(username, estado, paciente);
        } else if ("PACIENTE".equals(usuarioLogeado.getRol())) {
            blocks = citaService.getHistoricoCitasPacienteComoBlocks(username, estado, doctor);
        }

        model.addAttribute("historicoCitas", blocks);
        return "perfil";
    }

    @GetMapping("/perfilMedico/{medicoId}")
    public String perfilMedico(@PathVariable Long medicoId, Authentication authentication, Model model) {
        Optional<Usuario> medicoOpt = usuarioService.findUsuarioById(medicoId);
        if (medicoOpt.isEmpty()) {
            return "redirect:/perfilMedico";
        }
        Usuario medico = medicoOpt.get();
        List<AppointmentBlock> blocks = citaService.getHorarioCompletoMedicoComoBlocks(medico.getLogin());

        model.addAttribute("medico", medico);
        model.addAttribute("citasBlocks", blocks);
        if(authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        return "perfilMedico";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login?logout";
    }
}
