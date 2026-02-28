package spring.tuto.flowdesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.tuto.flowdesk.dto.ListRequestProject;
import spring.tuto.flowdesk.dto.ProjectRequest;
import spring.tuto.flowdesk.dto.ServiceBookDto;
import spring.tuto.flowdesk.entities.Project;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.enums.ProjectStatus;
import spring.tuto.flowdesk.exceptions.RessourceNotFoundException;
import spring.tuto.flowdesk.jwt.utils.AuthUtils;
import spring.tuto.flowdesk.repositories.ProjectRepository;
import spring.tuto.flowdesk.repositories.ServiceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectServiceImplt implements ProjectService{

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthUtils userDetails;
    @Override
    public ServiceBookDto RequestService(Long serviceId) {

        String connectedUserEmail = userDetails.getUserEmail();
        User connectedUser = userDetails.getLoggedUser();

        spring.tuto.flowdesk.entities.Service checkServiceIfExist = serviceRepository.findById(serviceId).orElseThrow(
                ()-> new RessourceNotFoundException("Service","ServiceId",serviceId)
        );

        Project pendingProject = new Project();

        pendingProject.setService(checkServiceIfExist);
        pendingProject.setProjectName("Working On "+checkServiceIfExist.getServiceName());
        pendingProject.setProjectStatus(ProjectStatus.PENDING);
        pendingProject.setProjectOwner(connectedUser);


        projectRepository.save(pendingProject);


        ServiceBookDto serviceBooking = new ServiceBookDto();

        serviceBooking.setPrice(checkServiceIfExist.getServicePrice());
        serviceBooking.setBookingDate(LocalDate.now());
        serviceBooking.setRequestStatus(ProjectStatus.PENDING);
        serviceBooking.setUserEmail(connectedUserEmail);
        serviceBooking.setUserFirstName(connectedUser.getFirstName());
        serviceBooking.setUserLastName(connectedUser.getLastName());
        serviceBooking.setStatus(true);
        serviceBooking.setMessage("Your service request for '" + checkServiceIfExist.getServiceName() +
                "' has been received and is pending review. Reference ID: #" + pendingProject.getProjectId() +
                ". Our team will process your request within 2 business days. You will be notified once the project is approved.");


        return serviceBooking;
    }

    @Override
    public ListRequestProject getAllPendingReqests(
            Integer pageNumber, Integer pageSize, String sortBy, String sortMethod,
            String selectedStatus) {

        Sort sort = sortMethod.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber,pageSize,sort);
        Page<Project> pagedPendingProjects;

        String statusParam = selectedStatus == null ? "" : selectedStatus.trim();
        boolean hasSpecificStatus = !statusParam.isEmpty() && !"ALL".equalsIgnoreCase(statusParam);
        if(hasSpecificStatus){
            pagedPendingProjects = projectRepository.findByProjectStatus(ProjectStatus.valueOf(selectedStatus),page);

        }else{

            pagedPendingProjects = projectRepository.findAll(page);

        }

        List<Project> pendingProjects = pagedPendingProjects.getContent();



        List<ProjectRequest> sentList = pendingProjects.stream().map(p->{

            ProjectRequest bookedService = new ProjectRequest();
            bookedService.setUserEmail(p.getProjectOwner().getUserEmail());
            bookedService.setUserFirstName(p.getProjectOwner().getFirstName());
            bookedService.setUserLastName(p.getProjectOwner().getLastName());
            bookedService.setPrice(p.getService().getServicePrice());
            bookedService.setServiceName(p.getService().getServiceName());
            bookedService.setProjectId(p.getProjectId());
            bookedService.setProjectName(p.getProjectName());
            bookedService.setProjectStatus(p.getProjectStatus());
            bookedService.setProjectDescription(p.getProjectDDescription());


            return bookedService;

        }).toList();

        ListRequestProject pendingProjcectDetails = new ListRequestProject();
        pendingProjcectDetails.setAllRequestedProject(sentList);
        pendingProjcectDetails.setPageNumber(pagedPendingProjects.getNumber());
        pendingProjcectDetails.setLastPage(pagedPendingProjects.isLast());
        pendingProjcectDetails.setTotalPage(pagedPendingProjects.getTotalPages());
        pendingProjcectDetails.setTotalElements(pagedPendingProjects.getTotalElements());
        pendingProjcectDetails.setPageSize(pagedPendingProjects.getSize());



        return pendingProjcectDetails;


    }

    @Override
    public ProjectRequest updateServiceStatus(String newStatus, Long serviceId) {

        Project updatedProject = projectRepository.findById(serviceId).orElseThrow(
                ()-> new RessourceNotFoundException("Project","ProjectId",serviceId)
        );

        updatedProject.setProjectStatus(ProjectStatus.valueOf(newStatus));

        Project savedProject = projectRepository.save(updatedProject);

        ProjectRequest serviceBookDto = new ProjectRequest();

        serviceBookDto.setServiceName(updatedProject.getService().getServiceName());
        serviceBookDto.setProjectId(updatedProject.getProjectId());

        serviceBookDto.setProjectStatus(updatedProject.getProjectStatus());

        serviceBookDto.setProjectDescription("");
        serviceBookDto.setProjectName(updatedProject.getProjectName());


        serviceBookDto.setPrice(savedProject.getService().getServicePrice());
        serviceBookDto.setUserEmail(updatedProject.getProjectOwner().getUserEmail());
        serviceBookDto.setUserFirstName(updatedProject.getProjectOwner().getFirstName());
        serviceBookDto.setUserLastName(updatedProject.getProjectOwner().getLastName());




        return serviceBookDto;
    }
}
