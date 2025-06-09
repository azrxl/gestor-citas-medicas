package cr.ac.una.demologinspringboot.logic.schedule;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitaLogic;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ScheduleMapper {
    /**
     * Transforma el Map generado de CitaLogic a una lista de entidades Cita para poder persistirlas.
     * @param monthlyCitas Map con fecha y lista de CitaLogic.
     * @param loginMedico Identificador del médico asignado.
     * @return Lista de entidades Cita.
     */
    public List<Cita> transformToCitas(Map<String, List<CitaLogic>> monthlyCitas, String loginMedico) {
        List<Cita> citas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        if (monthlyCitas == null || monthlyCitas.isEmpty()) {
            return citas;
        }
        for (Map.Entry<String, List<CitaLogic>> entry : monthlyCitas.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            try {
                LocalDate fecha = LocalDate.parse(entry.getKey());
                for (CitaLogic cl : entry.getValue()) {
                    if (cl == null || cl.getHorainicio() == null || cl.getHorafin() == null) {
                        continue;
                    }
                    Cita cita = new Cita();
                    cita.setLoginMedico(loginMedico);
                    cita.setLoginPaciente(null);
                    cita.setFecha(fecha);
                    cita.setHoraInicio(LocalTime.parse(cl.getHorainicio(), formatter));
                    cita.setHoraFin(LocalTime.parse(cl.getHorafin(), formatter));
                    cita.setEstado("ACTIVA");
                    citas.add(cita);
                }
            } catch (Exception e) {
                System.err.println("Error al procesar la fecha: " + entry.getKey() + " - " + e.getMessage());
            }
        }
        return citas;
    }
}
