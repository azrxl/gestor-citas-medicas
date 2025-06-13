package cr.ac.una.demologinspringboot.dto.medico;

import cr.ac.una.demologinspringboot.dto.schedule.DiaDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class MedicoProfileDTO {
    private Long id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String login;
    private String especialidad;
    private BigDecimal costoConsulta;
    private String localidad;
    private Integer frecuenciaCita;
    private Map<String, DiaDTO> horario;
}

