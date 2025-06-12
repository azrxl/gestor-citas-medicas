package cr.ac.una.demologinspringboot.dto.perfil;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UsuarioProfileDTO {
    private Long id;
    private String login;
    private String nombre;
    private String apellido;
    private String rol;
    private String cedula;           // solo para MEDICO
    private String especialidad;     // solo para MEDICO
    private String localidad;        // solo para MEDICO
    private BigDecimal costoConsulta;// solo para MEDICO
}

