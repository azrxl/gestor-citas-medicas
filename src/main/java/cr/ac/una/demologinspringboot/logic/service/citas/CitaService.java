package cr.ac.una.demologinspringboot.logic.service.citas;

import cr.ac.una.demologinspringboot.data.CitaRepository;
import cr.ac.una.demologinspringboot.dto.ui.AppointmentBlock;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.exceptions.AppointmentConflictException;
import cr.ac.una.demologinspringboot.logic.exceptions.InvalidStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cr.ac.una.demologinspringboot.logic.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private static final List<String> VALIDOS_ESTADOS_CITA = List.of("ACTIVA", "PENDIENTE", "COMPLETADA", "CANCELADA");

    @Autowired
    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public Cita findCitaByIdOrThrow(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita con ID " + id + " no encontrada."));
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

    public List<Cita> findCitasFiltradasParaMedico(String medicoLogin, String estado) {
        List<Cita> todasLasCitas = citaRepository.findByLoginMedico(medicoLogin);
        if (estado == null || estado.trim().isEmpty()) {
            return todasLasCitas;
        }
        List<Cita> citasFiltradas = new ArrayList<>();
        for (Cita cita : todasLasCitas) {
            if (cita.getEstado().equals(estado)) {
                citasFiltradas.add(cita);
            }
        }
        return citasFiltradas;
    }

    // En CitaService.java

    /**
     * Obtiene el historial de citas para un usuario, aplicando filtros opcionales.
     * Este método encapsula la lógica de si el usuario es un MÉDICO o un PACIENTE.
     * @param login El login del usuario.
     * @param rol El rol del usuario.
     * @param estadoFiltro El estado por el cual filtrar (opcional).
     * @return Una lista de citas filtradas.
     */
    public List<Cita> findCitasParaUsuario(String login, String rol, String estadoFiltro) {
        List<Cita> citas;

        if ("MEDICO".equals(rol)) {
            citas = citaRepository.findByLoginMedico(login);
        } else if ("PACIENTE".equals(rol)) {
            citas = citaRepository.findByLoginPaciente(login);
        } else {
            return List.of();
        }

        return citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .filter(c -> estadoFiltro == null || estadoFiltro.isEmpty() || estadoFiltro.equalsIgnoreCase(c.getEstado()))
                .collect(Collectors.toList());
    }

    public Optional<Cita> findCitaById(Long id) {
        return citaRepository.findById(id);
    }

    public Cita reservarCita(Long id, String loginPaciente) {
        Cita cita = findCitaByIdOrThrow(id);
        if (!"ACTIVA".equals(cita.getEstado()) || cita.getLoginPaciente() != null) {
            throw new AppointmentConflictException("La cita con ID " + id + " no está disponible para ser reservada. Estado actual: " + cita.getEstado());
        }
        cita.setEstado("PENDIENTE");
        cita.setLoginPaciente(loginPaciente);

        return citaRepository.save(cita);
    }

    public Cita completarCita(Long id) {
        Cita cita = findCitaByIdOrThrow(id);
        if (!"PENDIENTE".equals(cita.getEstado())) {
            throw new InvalidStateException("Solo se pueden completar citas que están pendientes de atención.");
        }
        cita.setEstado("COMPLETADA");
        return citaRepository.save(cita);
    }

    public Cita cancelarCitaPorMedico(Long id) {
        Cita cita = findCitaByIdOrThrow(id);
        if (!"PENDIENTE".equals(cita.getEstado())) {
            throw new InvalidStateException("Solo se pueden cancelar citas que están pendientes de atención.");
        }
        cita.setEstado("CANCELADA");
        return citaRepository.save(cita);
    }

    // Reemplaza la lógica de filtrado del perfil del médico
    public List<AppointmentBlock> getHistoricoCitasMedicoComoBlocks(String medicoLogin, String filtroEstado, String filtroPaciente) {
        List<Cita> citas = citaRepository.findByLoginMedico(medicoLogin); // Obtener todas las citas

        List<Cita> citasFiltradas = citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .filter(c -> filtroEstado == null || filtroEstado.isEmpty() || filtroEstado.equals(c.getEstado()))
                .filter(c -> filtroPaciente == null || filtroPaciente.isEmpty() || (c.getLoginPaciente() != null && c.getLoginPaciente().toLowerCase().contains(filtroPaciente.toLowerCase())))
                .collect(Collectors.toList());

        return convertirCitasABlocks(citasFiltradas);
    }

    public List<AppointmentBlock> getHistoricoCitasPacienteComoBlocks(String pacienteLogin, String filtroEstado, String filtroDoctor) {
        List<Cita> citas = citaRepository.findByLoginPaciente(pacienteLogin);

        List<Cita> citasFiltradas = citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .filter(c -> filtroEstado == null || filtroEstado.isEmpty() || filtroEstado.equals(c.getEstado()))
                .filter(c -> filtroDoctor == null || filtroDoctor.isEmpty() || (c.getLoginMedico() != null && c.getLoginMedico().toLowerCase().contains(filtroDoctor.toLowerCase())))
                .collect(Collectors.toList());

        return convertirCitasABlocks(citasFiltradas);
    }

    public List<AppointmentBlock> getHorarioCompletoMedicoComoBlocks(String medicoLogin) {
        List<Cita> citas = citaRepository.findByLoginMedico(medicoLogin);
        return convertirCitasABlocks(citas);
    }


    private List<AppointmentBlock> convertirCitasABlocks(List<Cita> citas) {
        Map<LocalDate, List<Cita>> citasPorFecha = citas.stream()
                .collect(Collectors.groupingBy(Cita::getFecha, TreeMap::new, Collectors.toList()));

        return citasPorFecha.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
