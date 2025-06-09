package cr.ac.una.demologinspringboot.logic.schedule;

import cr.ac.una.demologinspringboot.dto.schedule.DiaDTO;
import cr.ac.una.demologinspringboot.dto.schedule.IntervaloDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


@Component
public class ScheduleParser {
    public String parse(Map<String, DiaDTO> horario) {
        // Usamos StringJoiner para una construcción de strings más eficiente y limpia.
        StringJoiner scheduleJoiner = new StringJoiner(";");

        // La lógica original se adapta para iterar sobre el Map.
        for (Map.Entry<String, DiaDTO> entry : horario.entrySet()) {
            DiaDTO dia = entry.getValue();
            List<String> intervals = new ArrayList<>();

            if (isValidInterval(dia.getManana())) {
                intervals.add(formatInterval(dia.getManana()));
            }
            if (isValidInterval(dia.getTarde())) {
                intervals.add(formatInterval(dia.getTarde()));
            }

            scheduleJoiner.add(String.join(",", intervals));
        }

        return scheduleJoiner.toString();
    }

    // ANÁLISIS Y CAMBIOS: Los métodos privados ahora operan sobre el DTO.
    private boolean isValidInterval(IntervaloDTO intervalo) {
        return intervalo != null &&
                intervalo.getInicio() != null && !intervalo.getInicio().isEmpty() &&
                intervalo.getFin() != null && !intervalo.getFin().isEmpty();
    }

    private String formatInterval(IntervaloDTO intervalo) {
        String hInicio = intervalo.getInicio().split(":")[0];
        String hFin = intervalo.getFin().split(":")[0];
        return hInicio + "-" + hFin;
    }
}
