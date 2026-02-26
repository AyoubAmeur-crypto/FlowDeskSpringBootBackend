package spring.tuto.flowdesk.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.tuto.flowdesk.config.ConstantValue;
import spring.tuto.flowdesk.dto.ListServicesDto;
import spring.tuto.flowdesk.dto.ServiceDto;
import spring.tuto.flowdesk.dto.ServiceDtoSummary;
import spring.tuto.flowdesk.exceptions.ResponseStructure;
import spring.tuto.flowdesk.services.ServiceEntityService;

import java.io.IOException;

@RestController
@RequestMapping("/api/service")
public class ServiceController {


    @Autowired
    ServiceEntityService serviceEntityService;




    @PostMapping("/createService/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> createService(@PathVariable Long categoryId,

                                                    @RequestPart(value = "image" , required = false) MultipartFile image ,
                                                    @RequestParam("serviceName") String serviceName,
                                                    @RequestParam(value = "serviceDescription") String description,
                                                    @RequestParam("servicePrice") Double price) throws IOException {

        ServiceDto newServiceDto = new ServiceDto();

        newServiceDto.setServiceName(serviceName);
        newServiceDto.setServiceDescription(description);
        newServiceDto.setServicePrice(price);

        ServiceDto serivce = serviceEntityService.createService(categoryId,newServiceDto,image);
        return ResponseEntity.status(HttpStatus.CREATED).body(serivce);


    }


    @GetMapping("/services")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ListServicesDto> getAllServices(
            @RequestParam(defaultValue = ConstantValue.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = ConstantValue.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = ConstantValue.SORT_BY_SERVICE, required = false) String sortBy,
            @RequestParam(defaultValue = ConstantValue.SORT_METHOD, required = false) String sortMethod,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword
    ){


        ListServicesDto wantedServicesList = serviceEntityService.getAllServices(pageNumber,pageSize,sortBy,sortMethod,categoryId,keyword);


        return ResponseEntity.status(HttpStatus.OK).body(wantedServicesList);
    }


    @PutMapping("/updateService/{serviceId}")
    public ResponseEntity<ServiceDtoSummary> updateService(

            @RequestPart(value = "serviceImage" , required = false) MultipartFile image ,
            @RequestParam(value = "serviceName" , required = false) String serviceName,
            @RequestParam(value = "serviceDescription" , required = false) String description,
            @RequestParam(value = "servicePrice" , required = false) Double price,
            @PathVariable Long serviceId
    ) throws IOException {

        ServiceDtoSummary response = serviceEntityService.updateService(serviceId,image,serviceName,description,price);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")

    @DeleteMapping("/deleteService/{serviceId}")
    public ResponseEntity<ResponseStructure> deleteService(
            @PathVariable Long serviceId
    ) throws IOException {

        ResponseStructure deletedService = serviceEntityService.deleteService(serviceId);

        return ResponseEntity.status(HttpStatus.OK).body(deletedService);


    }



}
