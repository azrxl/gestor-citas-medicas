package cr.ac.una.demologinspringboot.dto.auth.login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String token;
    private String rol;
    private boolean profileComplete;

    public LoginResponseDTO(String username, String token, String rol) {
        this.username = username;
        this.token = token;
        this.rol = rol;
        this.profileComplete = false;
    }
}
