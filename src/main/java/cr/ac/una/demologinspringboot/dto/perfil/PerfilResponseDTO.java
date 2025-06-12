package cr.ac.una.demologinspringboot.dto.perfil;

import lombok.Data;

import java.util.List;

@Data
public class PerfilResponseDTO {
    private UsuarioProfileDTO usuario;
    private List<AppointmentBlockDTO> historicoCitas;
}
