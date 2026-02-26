package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @NotBlank
    private String projectName;


    private String projectDDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;


    private LocalDate startDate;


    private LocalDate estimatedDate;


    @ManyToOne()
    @JoinColumn(name = "service_id")
    private Service service;

    @ElementCollection
    private List<String> attachements = new ArrayList<>();

    @OneToMany(mappedBy = "assignedProject",fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User projectOwner;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> freelancers;



    @OneToMany(mappedBy = "belongedProject",fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
    private List<String> columnStatus = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_id")
    private FeedBack projectFeedback;
}
