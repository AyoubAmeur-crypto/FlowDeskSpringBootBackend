package spring.tuto.flowdesk.services;

import org.springframework.stereotype.Service;
import spring.tuto.flowdesk.dto.ListRequestProject;
import spring.tuto.flowdesk.dto.ProjectRequest;
import spring.tuto.flowdesk.dto.ServiceBookDto;

@Service
public interface ProjectService {
     ServiceBookDto RequestService(Long serviceId);


    ListRequestProject getAllPendingReqests(Integer pageNumber, Integer pageSize, String sortBy, String sortMethod, String selectedStatus);

    ProjectRequest updateServiceStatus(String newStatus, Long serviceId);
}
