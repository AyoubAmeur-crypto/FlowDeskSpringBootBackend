package spring.tuto.flowdesk.jwt.authenticationResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {


    private String message;

    private boolean status;
}
