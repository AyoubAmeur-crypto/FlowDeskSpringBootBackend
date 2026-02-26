package spring.tuto.flowdesk.jwt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.exceptions.RessourceNotFoundException;
import spring.tuto.flowdesk.repositories.UserRepository;

@Component
public class AuthUtils {


    @Autowired
    UserRepository userRepository;


    public User getLoggedUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User wantedUser = userRepository.findByUserEmail(authentication.getName()).orElseThrow(
                ()-> new RessourceNotFoundException("User","UserEmail", authentication.getName())
        );

        return wantedUser;

    }

    public String getUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User wantedUser = userRepository.findByUserEmail(authentication.getName()).orElseThrow(
                ()-> new RessourceNotFoundException("User","UserEmail", authentication.getName())
        );

        return wantedUser.getUserEmail();

    }


    public Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User wantedUser = userRepository.findByUserEmail(authentication.getName()).orElseThrow(
                ()-> new RessourceNotFoundException("User","UserEmail", authentication.getName())
        );

        return wantedUser.getUserId();

    }


}
