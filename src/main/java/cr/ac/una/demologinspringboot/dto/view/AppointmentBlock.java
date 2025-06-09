package cr.ac.una.demologinspringboot.dto.view;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentBlock {
    private LocalDate date;
    private List<Cita> citas;

    public AppointmentBlock(LocalDate date, List<Cita> citas) {
        this.date = date;
        this.citas = citas;
    }

}
