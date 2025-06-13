package cr.ac.una.demologinspringboot.logic.schedule;

import cr.ac.una.demologinspringboot.dto.schedule.DiaDTO;
import cr.ac.una.demologinspringboot.dto.schedule.IntervaloDTO;
import org.springframework.stereotype.Component;

import java.util.*;


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

    public Map<String, DiaDTO> deserialize(String horarioSemanal) {
        if (horarioSemanal == null || horarioSemanal.isBlank()) {
            // Retornar un horario vacío de 7 días por defecto
            String[] diasSemana = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};
            Map<String, DiaDTO> mapa = new LinkedHashMap<>();

            for (String dia : diasSemana) {
                DiaDTO dto = new DiaDTO();
                dto.setManana(new IntervaloDTO());
                dto.setTarde(new IntervaloDTO());
                mapa.put(dia, dto);
            }

            return mapa;
        }

        // Continuar con la lógica normal si no es null
        String[] dias = horarioSemanal.split(";");
        String[] diasSemana = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};

        Map<String, DiaDTO> mapa = new LinkedHashMap<>();

        for (int i = 0; i < diasSemana.length; i++) {
            String dia = diasSemana[i];
            DiaDTO dto = new DiaDTO();
            dto.setManana(new IntervaloDTO());
            dto.setTarde(new IntervaloDTO());

            if (i >= dias.length || dias[i].isEmpty()) {
                mapa.put(dia, dto);
                continue;
            }

            String[] bloques = dias[i].split(",");
            if (bloques.length > 0 && bloques[0].contains("-")) {
                String[] partes = bloques[0].split("-");
                dto.getManana().setInicio(partes[0] + ":00");
                dto.getManana().setFin(partes[1] + ":00");
            }
            if (bloques.length > 1 && bloques[1].contains("-")) {
                String[] partes = bloques[1].split("-");
                dto.getTarde().setInicio(partes[0] + ":00");
                dto.getTarde().setFin(partes[1] + ":00");
            }

            mapa.put(dia, dto);
        }

        return mapa;
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
