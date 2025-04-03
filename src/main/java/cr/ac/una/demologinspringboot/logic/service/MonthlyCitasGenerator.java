package cr.ac.una.demologinspringboot.logic.service;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitaLogic;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitasLogic;
import cr.ac.una.demologinspringboot.logic.entities.horario.Dia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MonthlyCitasGenerator {

    private final CitaRepository citaRepository;

    @Autowired
    public MonthlyCitasGenerator(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    /**
     * Guarda una lista de citas en el repositorio.
     */
    public void saveCitas(List<Cita> citas) {
        citaRepository.saveAll(citas);
    }

    /**
     * Genera las citas para todo el mes, iniciando en el lunes de la siguiente semana.
     * @param horarioSemanal El string con el horario semanal (ej.: "08-10,01-03;08-10,01-03;08-01,01-03;08-10,01-03;08-08;;;")
     * @param frecuencia La duración en minutos de cada cita.
     * @return Un Map donde la clave es la fecha (String, formato ISO) y el valor es la lista de citas disponibles para ese día.
     */
    public Map<String, List<CitaLogic>> generateMonthlyCitas(String horarioSemanal, int frecuencia) {
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

    /**
     * Transforma el Map generado de CitaLogic a una lista de entidades Cita para poder persistirlas.
     * @param monthlyCitas Map con fecha y lista de CitaLogic.
     * @param loginMedico Identificador del médico asignado.
     * @return Lista de entidades Cita.
     */
    public List<Cita> transformToCitas(Map<String, List<CitaLogic>> monthlyCitas, String loginMedico) {
        List<Cita> citas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm"); // Permite "8:00" y "08:00"

        for (Map.Entry<String, List<CitaLogic>> entry : monthlyCitas.entrySet()) {
            LocalDate fecha = LocalDate.parse(entry.getKey());
            for (CitaLogic cl : entry.getValue()) {
                Cita cita = new Cita();
                cita.setLoginMedico(loginMedico);
                cita.setLoginPaciente("DISPONIBLE"); // Indica que aún no ha sido asignada
                cita.setFecha(fecha);

                // Ajuste para parsear la hora
                cita.setHoraInicio(LocalTime.parse(cl.getHorainicio(), formatter));
                cita.setHoraFin(LocalTime.parse(cl.getHorafin(), formatter));

                cita.setEstado("Disponible");
                citas.add(cita);
            }
        }
        return citas;
    }


    public void generateAndSaveMonthlyCitas(String horarioSemanal, int frecuencia, String loginMedico) {
        // Generar las citas del mes
        Map<String, List<CitaLogic>> monthlyCitas = generateMonthlyCitas(horarioSemanal, frecuencia);

        // Convertirlas en entidades Cita
        List<Cita> citasParaGuardar = transformToCitas(monthlyCitas, loginMedico);

        // Guardarlas en el repositorio
        saveCitas(citasParaGuardar);
    }
}
