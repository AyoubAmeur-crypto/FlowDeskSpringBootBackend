package spring.tuto.flowdesk.jwt.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.tuto.flowdesk.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImplt implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    Collection<? extends GrantedAuthority> authorities;


    public static UserDetailsImplt build(User user){

        List<GrantedAuthority> authoritiesGranted = user.getUserRoles().stream().map(
                r-> new SimpleGrantedAuthority(r.getRole().name())
        ).collect(Collectors.toList());

        return new UserDetailsImplt(user.getUserId(),user.getFirstName(),user.getLastName(),user.getUserPassword(),user.getUserEmail(), user.getUserPhoneNumber(),user.isEnabled(),user.isAccountNonExpired(),user.isAccountNonLocked(),user.isCredentialsNonExpired(),authoritiesGranted);


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o){

        if(o == this)
            return true;
        if(o==null || getClass() != o.getClass())
            return false;
        UserDetailsImplt user = (UserDetailsImplt) o;

        return Objects.equals(id,user.getId());  }
}
