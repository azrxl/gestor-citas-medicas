package cr.ac.una.demologinspringboot.presentation.auth;


import cr.ac.una.demologinspringboot.dto.auth.login.LoginRequestDTO;
import cr.ac.una.demologinspringboot.dto.auth.login.LoginResponseDTO;
import cr.ac.una.demologinspringboot.dto.auth.register.RegistroRequestDTO;
import cr.ac.una.demologinspringboot.dto.entities.UsuarioDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import cr.ac.una.demologinspringboot.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUsuario(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getLogin(),
                        loginRequestDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);
        Usuario u = usuarioService.findUsuarioByLoginOrThrow(loginRequestDTO.getLogin());
        boolean complete = u.getEspecialidad() != null
                && u.getLocalidad() != null
                && u.getFrecuenciaCita() != null
                && u.getHorarioSemanal() != null;
        return ResponseEntity.ok(new LoginResponseDTO(loginRequestDTO.getLogin(), token, u.getRol()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody @Valid RegistroRequestDTO registroRequestDTO) {
        Usuario usuario = new Usuario(registroRequestDTO);
        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>("Usuario registrado correctamente", HttpStatus.CREATED);
    }
}
