package cr.ac.una.demologinspringboot.presentation.auth;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "expired", required = false) String expired,
                               @RequestParam(value = "notApproved", required = false) String notApproved,
                               Model model) {
        if (error != null) {
            model.addAttribute("mensaje", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión correctamente.");
        }
        if (expired != null) {
            model.addAttribute("mensaje", "La sesión ha expirado. Por favor, inicia sesión nuevamente.");
        }
        if (notApproved != null) {
            model.addAttribute("mensaje", "Su cuenta aún no ha sido aprobada por un administrador.");
        }
        return "login";
    }
}
