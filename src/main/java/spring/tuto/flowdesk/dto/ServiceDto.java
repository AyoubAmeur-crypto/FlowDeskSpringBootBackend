package spring.tuto.flowdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ServiceDto {


    private Long serviceId;
    @NotBlank
    private String serviceName;
    @NotBlank
    private String serviceDescription;

    private String serviceImage;
    @NotNull
    private double servicePrice;


    private CategoryDtoSimplified category;
}
