package cr.ac.una.demologinspringboot.logic.schedule;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ScheduleParser {
    public StringBuilder parse(HttpServletRequest request) {
        List<String> diasSemana = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        StringBuilder scheduleBuilder = new StringBuilder();

        for (String dia : diasSemana) {
            String mananaInicio = request.getParameter(dia + "MananaInicio");
            String mananaFin = request.getParameter(dia + "MananaFin");
            String tardeInicio = request.getParameter(dia + "TardeInicio");
            String tardeFin = request.getParameter(dia + "TardeFin");
            List<String> intervals = new ArrayList<>();
            if (isValidInterval(mananaInicio, mananaFin)) {
                intervals.add(formatInterval(mananaInicio, mananaFin));
            }
            if (isValidInterval(tardeInicio, tardeFin)) {
                intervals.add(formatInterval(tardeInicio, tardeFin));
            }
            String daySchedule = String.join(",", intervals);
            scheduleBuilder.append(daySchedule).append(";");
        }
        return scheduleBuilder;
    }

    private boolean isValidInterval(String inicio, String fin) {
        return inicio != null && !inicio.isEmpty() && fin != null && !fin.isEmpty();
    }

    private String formatInterval(String inicio, String fin) {
        String hInicio = inicio.split(":")[0];
        String hFin = fin.split(":")[0];
        return hInicio + "-" + hFin;
    }
}
