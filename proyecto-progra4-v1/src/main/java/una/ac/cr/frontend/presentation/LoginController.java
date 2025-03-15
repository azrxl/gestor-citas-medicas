package una.ac.cr.frontend.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login") //dirige la peticion (GET) hacia la ruta login
    //Envia la pagina al cliente que envia la solicitud de verla
    public String mostrarLogin() {
        return "login"; //retorna el html del login (login.html)
    }

    @PostMapping("/login") //recibe las credenciales del usuario
    public String procesarLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        // verificacion de prueba
        if (username.equals("admin") && password.equals("admin")) {
            model.addAttribute("username", username);
            return "bienvenida"; //buscara la pagina principal al iniciar sesion
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login"; //redirige a la misma pagina de login si es incorrecto
        }
    }
}
