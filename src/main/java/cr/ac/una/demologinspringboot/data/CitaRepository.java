package cr.ac.una.demologinspringboot.data;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByLoginMedico(String loginMedico);
    List<Cita> findByLoginPaciente(String paciente);
    List<Cita> findByLoginMedicoAndEstado(String medico, String estado);
    List<Cita> findByLoginMedicoAndEstadoNot(String login, String estado);

}
