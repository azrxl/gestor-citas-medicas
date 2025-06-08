package cr.ac.una.demologinspringboot.logic.service.citas;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.logic.schedule.ScheduleMapper;
import cr.ac.una.demologinspringboot.logic.schedule.ScheduleParser;
import cr.ac.una.demologinspringboot.logic.schedule.Scheduler;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.horario.CitaLogic;
import jakarta.servlet.http.HttpServletRequest;
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
        Map<String, List<CitaLogic>> monthlyCitas = scheduler.generateSchedule(horarioSemanal, frecuencia);
        List<Cita> citasParaGuardar = scheduleMapper.transformToCitas(monthlyCitas, loginMedico);
        citaRepository.saveAll(citasParaGuardar);
    }

    public StringBuilder scheduleParser(HttpServletRequest request) {
        return scheduleParser.parse(request);
    }
}
