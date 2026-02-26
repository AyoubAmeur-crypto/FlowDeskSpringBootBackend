package spring.tuto.flowdesk.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comment {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;


    private String content;

    private LocalDate createdAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User commentWriter;


    @ManyToOne()
    @JoinColumn(name = "taskId")
    private Task belongedTask;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project belongedProject;
}
