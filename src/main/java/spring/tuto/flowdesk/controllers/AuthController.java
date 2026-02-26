package spring.tuto.flowdesk.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import spring.tuto.flowdesk.entities.Role;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.enums.AppRole;
import spring.tuto.flowdesk.exceptions.ApiException;
import spring.tuto.flowdesk.exceptions.ResponseStructure;
import spring.tuto.flowdesk.exceptions.RessourceNotFoundException;
import spring.tuto.flowdesk.jwt.authenticationResponse.LoginResponse;
import spring.tuto.flowdesk.jwt.authenticationResponse.RegisterResponse;
import spring.tuto.flowdesk.jwt.authentificationRequest.LoginRequest;
import spring.tuto.flowdesk.jwt.authentificationRequest.RegisterRequest;
import spring.tuto.flowdesk.jwt.services.JwtUtils;
import spring.tuto.flowdesk.jwt.services.UserDetailsImplt;
import spring.tuto.flowdesk.repositories.RoleRepository;
import spring.tuto.flowdesk.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;




    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){

        if(userRepository.existsByUserEmail(request.getEmail()))
            throw new ApiException("Email Already Exist! ,Try another One");
        if(userRepository.existsByUserPhoneNumber(request.getPhoneNumber()))
            throw  new ApiException("Phone Number Already Exists! ,Try another One");

        User newUser = new User();

        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setUserEmail(request.getEmail());
        newUser.setUserPhoneNumber(request.getPhoneNumber());
        newUser.setUserPassword(passwordEncoder.encode(request.getPassword()));

        if(request.getRoles() == null || request.getRoles().isEmpty()){

            Role basicRole = roleRepository.findByRole(AppRole.ROLE_CLIENT).orElseThrow(
                    ()-> new RessourceNotFoundException("Role","RoleName", String.valueOf(AppRole.ROLE_CLIENT))
            );



            newUser.getUserRoles().add(basicRole);

        }else{

            request.getRoles().forEach(
                    role -> {

                        switch (role) {
                            case "admin" :
                                Role adminRole = roleRepository.findByRole(AppRole.ROLE_ADMIN).orElseThrow(
                                        ()-> new RessourceNotFoundException("Role","RoleName", String.valueOf(AppRole.ROLE_CLIENT))
                                );

                                newUser.getUserRoles().add(adminRole);

                                break;

                            case "freelancer" :
                                Role freelancer = roleRepository.findByRole(AppRole.ROLE_FREELANCER).orElseThrow(
                                        ()-> new RessourceNotFoundException("Role","RoleName", String.valueOf(AppRole.ROLE_CLIENT))
                                );

                                newUser.getUserRoles().add(freelancer);

                                break;

                            default:
                                Role clientRole = roleRepository.findByRole(AppRole.ROLE_CLIENT).orElseThrow(
                                        ()-> new RessourceNotFoundException("Role","RoleName", String.valueOf(AppRole.ROLE_CLIENT))
                                );

                                newUser.getUserRoles().add(clientRole);


                        }

                    }
            );
        }


        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("User Has Been Created Successfuly",true));








    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signInRequest(@RequestBody @Valid LoginRequest loginRequest){

        User user = userRepository.findByUserEmail(loginRequest.getEmail()).orElseThrow(
                ()-> new ApiException("User Not Found!")
        );
        Authentication authentication;
        try {

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            // Wrong email or password
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Credentials");
            errorResponse.put("message", "Email or password is incorrect. Please try again.");
            errorResponse.put("status", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (DisabledException e) {
            // Account is disabled
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account Disabled");
            errorResponse.put("message", "Your account has been disabled. Please contact support.");
            errorResponse.put("status", false);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

        } catch (LockedException e) {
            // Account is locked
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account Locked");
            errorResponse.put("message", "Your account has been locked due to too many failed login attempts.");
            errorResponse.put("status", false);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

        } catch (AuthenticationException e) {
            // Other authentication errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication Failed");
            errorResponse.put("message", "Unable to authenticate. Please try again.");
            errorResponse.put("status", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplt userDetails = (UserDetailsImplt) authentication.getPrincipal();

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);


        List<String> roles = authentication.getAuthorities().stream().map(
                r -> r.getAuthority()
        ).toList();


        LoginResponse loginResponse = new LoginResponse(userDetails.getId(),userDetails.getFirstName(),userDetails.getLastName(),userDetails.getUsername(),userDetails.getPhoneNumber(),roles);


        return ResponseEntity.
                status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(loginResponse);




    }


    @PostMapping("/signOut")
    public ResponseEntity<ResponseStructure> signout(){

        ResponseCookie cleanCookie = jwtUtils.generateCleanJwtCookie();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE,cleanCookie.toString())
                .body(new ResponseStructure("You've Been Logout Successfuly",true));


    }



}
