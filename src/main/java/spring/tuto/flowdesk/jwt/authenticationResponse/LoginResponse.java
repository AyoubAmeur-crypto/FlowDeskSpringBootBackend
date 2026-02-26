package spring.tuto.flowdesk.jwt.authenticationResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {


    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private List<String> roles = new ArrayList<>();

}
