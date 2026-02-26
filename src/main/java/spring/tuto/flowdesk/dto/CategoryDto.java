package spring.tuto.flowdesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;
    @NotBlank(message = "must enter a categoryName to continue")
    private String categoryName;


    private LocalDate lastEdited;
    List<ServiceDto> services;

}
