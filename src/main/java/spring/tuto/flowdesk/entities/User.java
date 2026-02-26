package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;




    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(message = "must enter a valid email")
    @NotBlank
    private String userEmail;


    @NotBlank
    @Size(min = 8 , message = "minimum password must have 8 characters")
    private String userPassword;


    private String userPhoneNumber;

    @OneToMany(mappedBy = "projectOwner",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<Project> projects;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
                    name = "user_roles",
                    inverseJoinColumns = {@JoinColumn(name = "role_id")}
                    ,joinColumns = {@JoinColumn(name = "user_id")}

            )
    Set<Role> userRoles = new HashSet<>();

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    public User(Long userId, String firstName, String lastName, String userEmail, String userPassword, String userPhoneNumber, Set<Role> userRoles, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userRoles = userRoles;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public User(String firstName, String lastName, String userEmail, String userPassword, Set<Role> userRoles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRoles = userRoles;
    }

    public User(String firstName, String lastName, String userEmail, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
