package cr.ac.una.demologinspringboot.dto.auth;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String login;
    private String password;
}
