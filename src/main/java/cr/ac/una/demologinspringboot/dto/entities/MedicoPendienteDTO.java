package cr.ac.una.demologinspringboot.dto.entities;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

@Data
public class MedicoPendienteDTO {
    private Long id;
    private String login;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private boolean aprobado;

    public MedicoPendienteDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.nombre= usuario.getNombre();
        this.apellidos= usuario.getApellido();
        this.especialidad= usuario.getEspecialidad();
        this.aprobado= usuario.getAprobado();
    }
}
