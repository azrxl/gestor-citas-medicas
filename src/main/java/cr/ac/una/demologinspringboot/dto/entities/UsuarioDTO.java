package cr.ac.una.demologinspringboot.dto.entities;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String cedula;
    private String nombre;
    private String apellido;
    private String login;
    private boolean aprobado;
    private String rol;

    public UsuarioDTO(Usuario usuario) {
        this.cedula = usuario.getCedula();
        this.login = usuario.getLogin();
        this.rol = usuario.getRol();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.aprobado = usuario.getAprobado();
    }
}
