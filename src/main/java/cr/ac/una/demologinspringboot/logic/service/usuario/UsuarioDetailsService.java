package cr.ac.una.demologinspringboot.logic.service.usuario;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.data.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        boolean disabled = "MEDICO".equals(usuario.getRol()) && !usuario.getAprobado();

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .disabled(disabled) // Signal account disabled
                .build();
    }
}
