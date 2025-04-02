package cr.ac.una.demologinspringboot.presentation.auth;

import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    //@Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
