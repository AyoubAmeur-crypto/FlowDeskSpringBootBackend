package spring.tuto.flowdesk.jwt.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.repositories.UserRepository;

@Service
public class UserDetailsServiceImplt implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetailsImplt loadUserByUsername(String email) throws UsernameNotFoundException {
        User checkUser = userRepository.findByUserEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("user not found with this email :"+email)
        );


        return UserDetailsImplt.build(checkUser);
    }
}
