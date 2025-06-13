package cr.ac.una.demologinspringboot.logic.service.citas;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.dto.medico.MedicoUpdateRequestDTO;
import cr.ac.una.demologinspringboot.dto.schedule.DiaDTO;
import cr.ac.una.demologinspringboot.logic.exceptions.InvalidScheduleFormatException;
import cr.ac.una.demologinspringboot.logic.schedule.ScheduleMapper;
import cr.ac.una.demologinspringboot.logic.schedule.ScheduleParser;
import cr.ac.una.demologinspringboot.logic.schedule.Scheduler;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitaLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SchedulerService {

    private final CitaRepository citaRepository;
    private final Scheduler scheduler;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleParser scheduleParser;

    @Autowired
    public SchedulerService(CitaRepository citaRepository, Scheduler scheduler, ScheduleMapper scheduleMapper, ScheduleParser scheduleParser) {
        this.citaRepository = citaRepository;
        this.scheduler = scheduler;
        this.scheduleMapper = scheduleMapper;
        this.scheduleParser = scheduleParser;
    }

    public void generateAndSaveMonthlyCitas(String horarioSemanal, int frecuencia, String loginMedico) {
        // ANÁLISIS Y CAMBIOS: Se añaden validaciones de los argumentos de entrada.
        if (horarioSemanal == null || horarioSemanal.trim().isEmpty()) {
            throw new InvalidScheduleFormatException("El horario semanal no puede estar vacío.");
        }
        if (frecuencia <= 0) {
            throw new IllegalArgumentException("La frecuencia de la cita debe ser un número positivo.");
        }
        if (loginMedico == null || loginMedico.trim().isEmpty()) {
            throw new IllegalArgumentException("El login del médico no puede estar vacío.");
        }

        try {
            Map<String, List<CitaLogic>> monthlyCitas = scheduler.generateSchedule(horarioSemanal, frecuencia);
            List<Cita> citasParaGuardar = scheduleMapper.transformToCitas(monthlyCitas, loginMedico);

            citaRepository.saveAll(citasParaGuardar);

        } catch (Exception e) {
            throw new InvalidScheduleFormatException("Error al procesar el formato del horario: " + e.getMessage(), e);
        }
    }

        public String scheduleParser(Map<String, DiaDTO> horario) {
            return scheduleParser.parse(horario);
    }

}
