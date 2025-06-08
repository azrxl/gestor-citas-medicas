package cr.ac.una.demologinspringboot.logic.service.citas;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.dto.AppointmentBlock;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    @Autowired
    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public List<Cita> findCitasByLoginMedico(String loginMedico) {
        return citaRepository.findByLoginMedico(loginMedico);
    }

    public List<Cita> findCitasByLoginPaciente(String loginPaciente) {
        return citaRepository.findByLoginPaciente(loginPaciente);
    }

    public List<Cita> findCitasActivasByMedico(String loginMedico) {
        return citaRepository.findByLoginMedicoAndEstado(loginMedico, "ACTIVA");
    }

    public Optional<Cita> findCitaById(Long id) {
        return citaRepository.findById(id);
    }

    public void actualizarEstadoCita(Long id, String nuevoEstado, String loginPaciente) {
        Optional<Cita> optionalCita = citaRepository.findById(id);
        if (optionalCita.isPresent()) {
            Cita cita = optionalCita.get();
            cita.setEstado(nuevoEstado);
            cita.setLoginPaciente(loginPaciente);
            citaRepository.save(cita);
        }
    }
    public void reservarCita(Long id, String loginPaciente) {
        Optional<Cita> cita = this.findCitaById(id);
        if(cita.isPresent() && "ACTIVA".equals(cita.get().getEstado())) {
            this.actualizarEstadoCita(id, "PENDIENTE", loginPaciente);
        } else {
            throw new IllegalStateException("La cita no está disponible para ser reservada.");
        }
    }

    public void completarCita(Long id) {
        Optional<Cita> optionalCita = citaRepository.findById(id);
        if (optionalCita.isPresent()) {
            Cita cita = optionalCita.get();
            cita.setEstado("COMPLETADA");
            citaRepository.save(cita);
        }
    }

    public void cancelarCitaPorMedico(Long id) {
        Optional<Cita> optionalCita = citaRepository.findById(id);
        if (optionalCita.isPresent()) {
            Cita cita = optionalCita.get();
            cita.setEstado("CANCELADA");
            citaRepository.save(cita);
        }
    }
    // En CitaService.java

    // Reemplaza la lógica de filtrado del perfil del médico
    public List<AppointmentBlock> getHistoricoCitasMedicoComoBlocks(String medicoLogin, String filtroEstado, String filtroPaciente) {
        List<Cita> citas = citaRepository.findByLoginMedico(medicoLogin); // Obtener todas las citas

        // Filtrar en memoria (idealmente, esto se haría con una query de JPA Specification o QueryDSL)
        List<Cita> citasFiltradas = citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .filter(c -> filtroEstado == null || filtroEstado.isEmpty() || filtroEstado.equals(c.getEstado()))
                .filter(c -> filtroPaciente == null || filtroPaciente.isEmpty() || (c.getLoginPaciente() != null && c.getLoginPaciente().toLowerCase().contains(filtroPaciente.toLowerCase())))
                .collect(Collectors.toList());

        return convertirCitasABlocks(citasFiltradas);
    }

    // Reemplaza la lógica de filtrado del perfil del paciente
    public List<AppointmentBlock> getHistoricoCitasPacienteComoBlocks(String pacienteLogin, String filtroEstado, String filtroDoctor) {
        List<Cita> citas = citaRepository.findByLoginPaciente(pacienteLogin);

        List<Cita> citasFiltradas = citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .filter(c -> filtroEstado == null || filtroEstado.isEmpty() || filtroEstado.equals(c.getEstado()))
                .filter(c -> filtroDoctor == null || filtroDoctor.isEmpty() || (c.getLoginMedico() != null && c.getLoginMedico().toLowerCase().contains(filtroDoctor.toLowerCase())))
                .collect(Collectors.toList());

        return convertirCitasABlocks(citasFiltradas);
    }

    // Reemplaza la lógica de perfilMedico
    public List<AppointmentBlock> getHorarioCompletoMedicoComoBlocks(String medicoLogin) {
        List<Cita> citas = citaRepository.findByLoginMedico(medicoLogin);
        return convertirCitasABlocks(citas);
    }


    // El helper 'convertirABlocks' ahora vive en el servicio y es privado
    private List<AppointmentBlock> convertirCitasABlocks(List<Cita> citas) {
        Map<LocalDate, List<Cita>> citasPorFecha = citas.stream()
                .collect(Collectors.groupingBy(Cita::getFecha, TreeMap::new, Collectors.toList()));

        return citasPorFecha.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
