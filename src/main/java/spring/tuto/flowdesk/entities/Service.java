package spring.tuto.flowdesk.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @NotBlank
    private String serviceName;

    @NotBlank
    private String serviceDescription;

    @NotBlank
    private String imageUrl;


    @NotNull
    private double servicePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    Category category;
}
