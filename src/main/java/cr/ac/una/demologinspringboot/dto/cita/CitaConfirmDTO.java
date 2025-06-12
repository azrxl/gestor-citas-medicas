package cr.ac.una.demologinspringboot.dto.cita;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaConfirmDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
}
