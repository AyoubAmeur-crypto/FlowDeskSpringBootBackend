package spring.tuto.flowdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.entities.Service;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.enums.ProjectStatus;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private Long projectId;
    private String projectName;
    private String projectDescription;
    private ProjectStatus projectStatus;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String serviceName;
    private double price;
}
