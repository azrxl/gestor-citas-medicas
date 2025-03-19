package cr.ac.una.logic;

import cr.ac.una.data.*;
import cr.ac.una.logic.entities.*;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("service")
public class Service {
    /* ----------------------------------------------------------- */

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermisosPerfilRepository permisosPerfilRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    /* ----------------------------------------------------------- */

    public Iterable<Usuario> usuariosFindAll() {
        return usuarioRepository.findAll();
    }
    public Iterable<Permisosperfil> permisosPerfilFindAll() {
        return permisosPerfilRepository.findAll();
    }
    public Iterable<Medico> medicosFindAll() {
        return medicoRepository.findAll();
    }
    public Iterable<Especialidad> especialidadFindAll() {
        return especialidadRepository.findAll();
    }
    public Iterable<Localidad> localidadFindAll() {
        return localidadRepository.findAll();
    }
    public Iterable<Permiso> permisosFindAll() {
        return permisoRepository.findAll();
    }
    public Iterable<Perfil> perfilFindAll() {
        return perfilRepository.findAll();
    }

    /* ----------------------------------------------------------- */
}
