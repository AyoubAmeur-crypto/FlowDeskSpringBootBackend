package spring.tuto.flowdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Role;
import spring.tuto.flowdesk.enums.AppRole;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(AppRole appRole);
}
