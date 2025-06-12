package cr.ac.una.demologinspringboot.dto.auth.login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String token;

    public LoginResponseDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
