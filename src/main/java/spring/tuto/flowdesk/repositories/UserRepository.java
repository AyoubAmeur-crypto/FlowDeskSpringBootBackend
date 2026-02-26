package spring.tuto.flowdesk.repositories;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.tuto.flowdesk.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserEmail(String email);

    boolean existsByUserEmail(String user1);

    boolean existsByUserPhoneNumber(@Size(min = 10, max = 10, message = "phone number must have 10 characters") String phoneNumber);
}
