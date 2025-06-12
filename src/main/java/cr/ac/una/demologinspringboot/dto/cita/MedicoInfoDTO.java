package cr.ac.una.demologinspringboot.dto.cita;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicoInfoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String localidad;
    private BigDecimal costoConsulta;
}
