package spring.tuto.flowdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.FeedBack;

public interface FeedbackRepository extends JpaRepository<FeedBack,Long> {


}
