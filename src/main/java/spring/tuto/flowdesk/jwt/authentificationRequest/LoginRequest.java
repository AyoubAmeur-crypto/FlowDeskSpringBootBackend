package spring.tuto.flowdesk.jwt.authentificationRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {


    @Email(message = "must be a valid email")
    @NotBlank
    private String email;

    @NotBlank

    private String password;
}
