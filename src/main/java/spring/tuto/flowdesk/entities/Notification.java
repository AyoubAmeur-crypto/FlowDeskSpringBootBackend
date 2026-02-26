package spring.tuto.flowdesk.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.enums.NotificationType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @NotBlank
    private String notificationMessage;
    private boolean isRead;

    private LocalDate createdAt;
}
