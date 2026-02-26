package spring.tuto.flowdesk.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.tuto.flowdesk.entities.Project;
import spring.tuto.flowdesk.enums.ProjectStatus;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    Page<Project> findByProjectStatus(@NotNull ProjectStatus projectStatus, Pageable pageable);
}
