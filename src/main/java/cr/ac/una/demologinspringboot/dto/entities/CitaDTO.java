package cr.ac.una.demologinspringboot.dto.entities;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String medicoLogin;
    private String medicoNombreCompleto;
    private String pacienteLogin;
    private String pacienteNombreCompleto;

    public CitaDTO(Cita cita, Usuario medico, Usuario paciente) {
        this.id = cita.getId();
        this.fecha = cita.getFecha();
        this.horaInicio = cita.getHoraInicio();
        this.horaFin = cita.getHoraFin();
        this.estado = cita.getEstado();

        if (medico != null) {
            this.medicoLogin = medico.getLogin();
            this.medicoNombreCompleto = medico.getNombre() + " " + medico.getApellido();
        }

        if (paciente != null) {
            this.pacienteLogin = paciente.getLogin();
            this.pacienteNombreCompleto = paciente.getNombre() + " " + paciente.getApellido();
        }
    }
}