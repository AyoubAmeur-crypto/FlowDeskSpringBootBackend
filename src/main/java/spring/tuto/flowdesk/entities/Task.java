package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tasks")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @NotBlank
    private String taskTitle;


    @NotBlank
    private String taskDescription;


    private LocalDate startDate;

    @NotBlank
    private LocalDate deadLineDate;

    @ManyToOne()
    @JoinColumn(name = "projectId")
    private Project assignedProject;


    @ElementCollection
    private List<String> attachements = new ArrayList<>();

    private String taskStatus;


    @OneToMany(mappedBy = "belongedTask",fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();


}
