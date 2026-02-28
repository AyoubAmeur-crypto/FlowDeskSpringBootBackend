package spring.tuto.flowdesk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.tuto.flowdesk.config.ConstantValue;
import spring.tuto.flowdesk.dto.ListRequestProject;
import spring.tuto.flowdesk.dto.ProjectRequest;
import spring.tuto.flowdesk.dto.ServiceBookDto;
import spring.tuto.flowdesk.services.ProjectService;


@RestController
@RequestMapping("/api/project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;

    /** test if the pipline is working correctly**/
    /** test  the building with actions**/


    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/bookService/{serviceId}")
    public ResponseEntity<ServiceBookDto> bookService(@PathVariable Long serviceId){


         ServiceBookDto serviceBook = projectService.RequestService(serviceId);


        return ResponseEntity.status(HttpStatus.CREATED).body(serviceBook);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRequestedService")
    public ResponseEntity<ListRequestProject> getPendingRequests(
            @RequestParam(defaultValue = ConstantValue.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = ConstantValue.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = ConstantValue.SORT_BY_PROJECT, required = false) String sortBy,
            @RequestParam(defaultValue = ConstantValue.SORT_METHOD, required = false) String sortMethod,
            @RequestParam String selectedStatus
    ){

        ListRequestProject pendingUserRequests = projectService.getAllPendingReqests(pageNumber,pageSize,sortBy,sortMethod,selectedStatus);


        return ResponseEntity.status(HttpStatus.OK).body(pendingUserRequests);



    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateBookService/{serviceId}")
    public ResponseEntity<ProjectRequest> updateBookServiceStatus(
            @PathVariable Long serviceId,
            @RequestParam String newStatus
    ){
        ProjectRequest updatedService = projectService.updateServiceStatus(newStatus,serviceId);



        return ResponseEntity.status(HttpStatus.OK).body(updatedService);
    }
}
