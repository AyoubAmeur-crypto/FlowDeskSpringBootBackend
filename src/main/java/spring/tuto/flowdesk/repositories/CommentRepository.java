package spring.tuto.flowdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
