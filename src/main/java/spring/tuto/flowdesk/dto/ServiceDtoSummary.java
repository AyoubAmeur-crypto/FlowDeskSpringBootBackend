package spring.tuto.flowdesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDtoSummary {

    private Long serviceId;
    @NotBlank
    private String serviceName;
    @NotBlank
    private String serviceDescription;

    private String serviceImage;
    @NotNull
    private double servicePrice;
}
