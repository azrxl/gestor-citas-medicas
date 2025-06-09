package cr.ac.una.demologinspringboot.dto.auth;
import lombok.Data;

@Data
public class RegistroDTO {
    private String cedula;
    private String nombre;
    private String apellido;
    private String login;
    private String password;
    private String confirmPassword;
    private String rol;
}
