package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.enums.AppRole;

import java.util.List;

@Entity(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "user")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AppRole role;


    @ManyToMany(mappedBy = "userRoles")
    private List<User> user;

    public Role(AppRole role) {
        this.role = role;
    }
}
