package cr.ac.una.demologinspringboot.dto.medico;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicoPublicoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private BigDecimal costoConsulta;
    private String localidad;

    public MedicoPublicoDTO(Usuario usuario) {
        if (!"MEDICO".equals(usuario.getRol())) {
            throw new IllegalArgumentException("Este DTO solo puede ser usado para usuarios con rol MEDICO.");
        }
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.especialidad = usuario.getEspecialidad();
        this.costoConsulta = usuario.getCostoConsulta();
        this.localidad = usuario.getLocalidad();
    }
}
