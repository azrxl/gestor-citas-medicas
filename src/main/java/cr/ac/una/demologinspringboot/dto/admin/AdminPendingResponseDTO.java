package cr.ac.una.demologinspringboot.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminPendingResponseDTO {
    private String username;               // Nombre del admin
    private List<MedicoApprovalDTO> medicos;
}
