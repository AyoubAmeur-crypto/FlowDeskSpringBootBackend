package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    private String content;

    private int rating;

    @OneToOne(mappedBy = "projectFeedback")
    private Project project;
}
