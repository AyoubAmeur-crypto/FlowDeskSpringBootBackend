package spring.tuto.flowdesk.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCategoryName(@NotBlank String categoryName);

    boolean existsByCategoryNameIgnoreCase(@NotBlank(message = "must enter a categoryName to continue") String categoryName);
}
