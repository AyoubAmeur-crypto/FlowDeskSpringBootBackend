package spring.tuto.flowdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.tuto.flowdesk.entities.Category;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfCategoriesDto {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean lastPage;

    List<CategoryDto> allCategories = new ArrayList<>();
}
