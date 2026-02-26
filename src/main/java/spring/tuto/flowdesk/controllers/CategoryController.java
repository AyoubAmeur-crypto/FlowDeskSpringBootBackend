package spring.tuto.flowdesk.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.tuto.flowdesk.config.ConstantValue;
import spring.tuto.flowdesk.dto.CategoryDto;
import spring.tuto.flowdesk.dto.ListOfCategoriesDto;
import spring.tuto.flowdesk.services.CategoryServices;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {


    @Autowired
    CategoryServices categoryServices;

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryDto categoryDto){

        CategoryDto savedCategory = categoryServices.createCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);

    }



    @GetMapping("/allCategoires")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryDto>> getCategoires(){


        List<CategoryDto> allCategories = categoryServices.getCategories();


                return ResponseEntity.status(HttpStatus.OK).body(allCategories);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListOfCategoriesDto> getAllCategories(
            @RequestParam(defaultValue = ConstantValue.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = ConstantValue.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = ConstantValue.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = ConstantValue.SORT_METHOD, required = false) String sortMethod



    ){

        ListOfCategoriesDto categories = categoryServices.getAllCategories(pageNumber,pageSize,sortBy,sortMethod);

        return ResponseEntity.status(HttpStatus.OK).body(categories);

    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long categoryId){

        CategoryDto deleteCategory = categoryServices.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(deleteCategory);

    }

    @PutMapping("/updateCategory/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto ,@PathVariable Long categoryId){

        CategoryDto deleteCategory = categoryServices.updateCategory(categoryDto,categoryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(deleteCategory);

    }

}
