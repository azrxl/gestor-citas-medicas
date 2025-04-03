package cr.ac.una.demologinspringboot.dto;

import cr.ac.una.demologinspringboot.logic.entities.Cita;

import java.time.LocalDate;
import java.util.List;

public class AppointmentBlock {
    private LocalDate date;
    private List<Cita> citas;

    public AppointmentBlock(LocalDate date, List<Cita> citas) {
        this.date = date;
        this.citas = citas;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Cita> getCitas() {
        return citas;
    }
}
