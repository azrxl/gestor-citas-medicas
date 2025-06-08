package cr.ac.una.demologinspringboot.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicoDto {
    private String especialidad;
    private BigDecimal costoConsulta;
    private String localidad;
    private String horarioSemanal;
    private Integer frecuenciaCita;
}
