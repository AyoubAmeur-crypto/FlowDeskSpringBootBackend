package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank
    private String categoryName;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
    List<Service> services = new ArrayList<>();


    @NotNull
    private LocalDate lastEdited;


}
