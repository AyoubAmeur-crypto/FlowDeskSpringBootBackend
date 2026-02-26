package spring.tuto.flowdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.enums.ProjectStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBookDto {

    private boolean status;
    private String message;
    private Double price;
    private LocalDate bookingDate;
    private ProjectStatus requestStatus;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
}
