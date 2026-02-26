package spring.tuto.flowdesk.jwt.authentificationRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "must enter a firstName")
    private String firstName;
    @NotBlank(message = "must enter a lastName")
    private String lastName;

    @Email(message = "must be a valid email")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "must have at least 8 characters in password")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "password must contain 8 character at least one digit, one lowercase, one uppercase, one special character and no whitespace"
    )
    private String password;

    @Size(min = 10, max = 10, message = "phone number must have 10 characters")
    private String phoneNumber;


    private Set<String> roles = new HashSet<>();
}
