package cr.ac.una.demologinspringboot.logic.schedule;

import cr.ac.una.demologinspringboot.logic.entities.horario.CitaLogic;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitasLogic;
import cr.ac.una.demologinspringboot.logic.entities.horario.Dia;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Scheduler {
    /**
     * Genera las citas para todo el mes, iniciando en el lunes de la siguiente semana.
     * @param horarioSemanal El string con el horario semanal (ej.: "08-10,01-03;08-10,01-03;08-01,01-03;08-10,01-03;08-08;;;")
     * @param frecuencia La duración en minutos de cada cita.
     * @return Un Map donde la clave es la fecha (String, formato ISO) y el valor es la lista de citas disponibles para ese día.
     */
    public Map<String, List<CitaLogic>> generateSchedule(String horarioSemanal, int frecuencia) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.plusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate end = today.plusMonths(1);

        Map<String, List<CitaLogic>> monthlyCitas = new LinkedHashMap<>();
        CitasLogic citasLogic = new CitasLogic();

        LocalDate currentWeek = nextMonday;
        while (!currentWeek.isAfter(end)) {
            List<Dia> semana = citasLogic.EstimarSemanaCitas(horarioSemanal, frecuencia);
            for (int i = 0; i < semana.size(); i++) {
                LocalDate fecha = currentWeek.plusDays(i);
                if (!fecha.isAfter(end)) {
                    List<CitaLogic> citasDelDia = semana.get(i).getCitaLogics();
                    monthlyCitas.put(fecha.toString(), citasDelDia);
                }
            }
            currentWeek = currentWeek.plusWeeks(1);
        }
        return monthlyCitas;
    }
}
