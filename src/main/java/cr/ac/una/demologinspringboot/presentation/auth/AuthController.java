package cr.ac.una.demologinspringboot.presentation.auth;


import cr.ac.una.demologinspringboot.dto.auth.LoginRequestDTO;
import cr.ac.una.demologinspringboot.dto.auth.LoginResponseDTO;
import cr.ac.una.demologinspringboot.dto.auth.RegistroDTO;
import cr.ac.una.demologinspringboot.dto.entities.UsuarioDTO;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import cr.ac.una.demologinspringboot.security.JwtAuthFilter;
import cr.ac.una.demologinspringboot.security.JwtUtil;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
        return ResponseEntity.ok(new LoginResponseDTO(token, loginRequestDTO.getLogin()));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid RegistroDTO registroDTO) {
        Usuario usuario = new Usuario(registroDTO);
        Usuario registrado = usuarioService.registrarUsuario(usuario, registroDTO.getConfirmPassword());
        return new ResponseEntity<>(new UsuarioDTO(registrado), HttpStatus.CREATED);
    }
}
