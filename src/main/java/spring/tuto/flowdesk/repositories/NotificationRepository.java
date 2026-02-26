package spring.tuto.flowdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
