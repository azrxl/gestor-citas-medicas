package cr.ac.una.demologinspringboot.data;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    public List<Cita> findByLoginMedico(String loginMedico);
    public List<Cita> findByLoginPaciente(String paciente);
}
