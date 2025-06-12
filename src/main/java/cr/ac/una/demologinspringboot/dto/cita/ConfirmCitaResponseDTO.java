package cr.ac.una.demologinspringboot.dto.cita;

import lombok.Data;

@Data
public class ConfirmCitaResponseDTO {
    private CitaConfirmDTO cita;
    private MedicoInfoDTO medico;
}
