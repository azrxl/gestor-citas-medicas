package cr.ac.una.demologinspringboot.dto.view;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HomeViewModel {
    private List<Usuario> medicos;
    private List<String> especialidades;
    private List<String> ciudades;
    private Map<Long, List<AppointmentBlock>> citasPorMedico;
}
