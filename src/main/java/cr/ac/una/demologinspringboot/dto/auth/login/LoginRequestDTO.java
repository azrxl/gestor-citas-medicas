package cr.ac.una.demologinspringboot.dto.auth.login;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String login;
    private String password;
}
