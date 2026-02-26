package spring.tuto.flowdesk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoSimplified {

    private Long categoryId;
    private String categoryName;


    private LocalDate lastEdited;
}
