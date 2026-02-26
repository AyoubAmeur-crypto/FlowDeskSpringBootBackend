package spring.tuto.flowdesk.services;


import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.tuto.flowdesk.dto.ListServicesDto;
import spring.tuto.flowdesk.dto.ServiceDto;
import spring.tuto.flowdesk.dto.ServiceDtoSummary;
import spring.tuto.flowdesk.exceptions.ResponseStructure;

import java.io.IOException;

@Service
public interface ServiceEntityService {
    ServiceDto createService(Long categoryId,@Valid ServiceDto serviceDto, MultipartFile image) throws IOException;

    ListServicesDto getAllServices(Integer pageNumber, Integer pageSize, String sortBy, String sortMethod,Long categoryId,String keyword);


    ServiceDtoSummary updateService(Long serviceId, MultipartFile image, String serviceName, String description, Double price) throws IOException;

    ResponseStructure deleteService(Long serviceId) throws IOException;
}
