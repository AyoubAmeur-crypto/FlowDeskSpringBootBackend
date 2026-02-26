package spring.tuto.flowdesk.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Service;

public interface ServiceRepository extends JpaRepository<Service,Long> {
    Page<Service> findByServiceNameContainingIgnoreCaseAndCategoryCategoryId(String trim, Long categoryId, Pageable page);


    Page<Service> findByServiceNameContainingIgnoreCase(@NotBlank String serviceName, Pageable pageable);

    Page<Service> findByCategoryCategoryId(Long categoryId, Pageable page);
}
