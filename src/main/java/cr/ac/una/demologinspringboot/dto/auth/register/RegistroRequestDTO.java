package cr.ac.una.demologinspringboot.dto.auth.register;
import lombok.Data;

@Data
public class RegistroRequestDTO {
    private String cedula;
    private String nombre;
    private String apellido;
    private String login;
    private String password;
    private String confirmPassword;
    private String rol;
}
