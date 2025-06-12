package cr.ac.una.demologinspringboot.dto.admin;

import lombok.Data;

@Data
public class MedicoApprovalDTO {
    private Long id;
    private String login;
    private String nombre;
    private String apellido;
    private Boolean aprobado;
}
