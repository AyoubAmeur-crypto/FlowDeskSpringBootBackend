package spring.tuto.flowdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
