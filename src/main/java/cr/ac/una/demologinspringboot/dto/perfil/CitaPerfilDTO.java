package cr.ac.una.demologinspringboot.dto.perfil;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaPerfilDTO {
    private Long id;
    private String loginMedico;
    private String loginPaciente;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
}
