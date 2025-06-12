package cr.ac.una.demologinspringboot.presentation.filtros;

import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filtros")
public class FiltroController {

    private final UsuarioService usuarioService;

    public FiltroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<String>> getEspecialidades() {
        return ResponseEntity.ok(usuarioService.findUsuarioDistinctEspecialidad());
    }

    @GetMapping("/ciudades")
    public ResponseEntity<List<String>> getCiudades() {
        return ResponseEntity.ok(usuarioService.findUsuarioDistinctLocalidades());
    }
}