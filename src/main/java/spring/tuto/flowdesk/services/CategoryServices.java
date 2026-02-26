package spring.tuto.flowdesk.services;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import spring.tuto.flowdesk.dto.CategoryDto;
import spring.tuto.flowdesk.dto.ListOfCategoriesDto;

import java.util.List;

@Service
public interface CategoryServices {
    CategoryDto createCategory(@Valid CategoryDto categoryDto);

    ListOfCategoriesDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortMethod);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(@Valid CategoryDto categoryDto, Long categoryId);

    List<CategoryDto> getCategories();
}
