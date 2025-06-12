package cr.ac.una.demologinspringboot.dto.perfil;

import cr.ac.una.demologinspringboot.dto.entities.CitaDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentBlockDTO {
    private LocalDate date;
    private List<CitaPerfilDTO> citas;
}
