package spring.tuto.flowdesk.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.tuto.flowdesk.dto.CategoryDto;
import spring.tuto.flowdesk.dto.ListOfCategoriesDto;
import spring.tuto.flowdesk.dto.ServiceDto;
import spring.tuto.flowdesk.entities.Category;
import spring.tuto.flowdesk.exceptions.ApiException;
import spring.tuto.flowdesk.exceptions.RessourceNotFoundException;
import spring.tuto.flowdesk.repositories.CategoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryServiceImplt implements CategoryServices{

    @Autowired
    CategoryRepository categoryRepository;





    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        if(categoryRepository.existsByCategoryNameIgnoreCase(categoryDto.getCategoryName()))
            throw new ApiException("Category name : "+categoryDto.getCategoryName()+" ,already Exists! ,try another One");
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryDto.getCategoryName());
        newCategory.setLastEdited(LocalDate.now());

        Category savedOne = categoryRepository.save(newCategory);

        CategoryDto result = new CategoryDto();
        result.setCategoryId(savedOne.getCategoryId());
        result.setCategoryName(savedOne.getCategoryName());
        result.setLastEdited(savedOne.getLastEdited());

        return result;
    }

    @Override
    public ListOfCategoriesDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortMethod) {
        Sort sort = sortMethod.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> allCategoriesPage = categoryRepository.findAll(page);

        List<Category> allCategories = allCategoriesPage.getContent();

        if(allCategories.isEmpty()){

            throw  new ApiException("there is no category exists");
        }

        ListOfCategoriesDto categoriesDtos = new ListOfCategoriesDto();


        categoriesDtos.setLastPage(allCategoriesPage.isLast());
        categoriesDtos.setPageNumber(allCategoriesPage.getNumber());
        categoriesDtos.setTotalPage(allCategoriesPage.getTotalPages());
        categoriesDtos.setTotalElements(allCategoriesPage.getTotalElements());
        categoriesDtos.setPageSize(allCategoriesPage.getSize());

        List<CategoryDto> categoryDtos = allCategories.stream().map(c -> {
            CategoryDto convertedCategory = new CategoryDto();
            convertedCategory.setCategoryId(c.getCategoryId());
            convertedCategory.setCategoryName(c.getCategoryName());
            convertedCategory.setLastEdited(c.getLastEdited());

            List<ServiceDto> services = c.getServices().stream().map(s -> {
                ServiceDto serviceDto = new ServiceDto();
                serviceDto.setServiceId(s.getServiceId());
                serviceDto.setServiceName(s.getServiceName());
                serviceDto.setServiceDescription(s.getServiceDescription());
                serviceDto.setServiceImage(s.getImageUrl());
                serviceDto.setServicePrice(s.getServicePrice());
                return serviceDto;
            }).toList();

            convertedCategory.setServices(services);
            return convertedCategory;
        }).toList();

        categoriesDtos.setAllCategories(categoryDtos);

        return categoriesDtos;
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {

        Category checkCategoryIfExist = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new RessourceNotFoundException("Category","CategoryId",categoryId)

        );

        CategoryDto result = new CategoryDto();
        result.setCategoryId(checkCategoryIfExist.getCategoryId());
        result.setCategoryName(checkCategoryIfExist.getCategoryName());
        result.setLastEdited(checkCategoryIfExist.getLastEdited());

        categoryRepository.delete(checkCategoryIfExist);

        return result;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category checkUpdatedCategoryIfExist = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new RessourceNotFoundException("Category","categoryId",categoryId)
        );

        if(categoryRepository.existsByCategoryNameIgnoreCase(categoryDto.getCategoryName())){
            throw  new ApiException(categoryDto.getCategoryName()+" ,already exists try another one!");
        }

        checkUpdatedCategoryIfExist.setCategoryName(categoryDto.getCategoryName());
        checkUpdatedCategoryIfExist.setLastEdited(LocalDate.now());

        Category savedOne = categoryRepository.save(checkUpdatedCategoryIfExist);

        CategoryDto result = new CategoryDto();
        result.setCategoryId(savedOne.getCategoryId());
        result.setCategoryName(savedOne.getCategoryName());
        result.setLastEdited(savedOne.getLastEdited());

        return result;
    }

    @Override
    public List<CategoryDto> getCategories() {

        List<Category> allWantedCategories = categoryRepository.findAll();

        List<CategoryDto> mappedCategories = allWantedCategories.stream().map(c -> {
            CategoryDto category = new CategoryDto();
            category.setCategoryId(c.getCategoryId());
            category.setCategoryName(c.getCategoryName());
            category.setLastEdited(c.getLastEdited());

            List<ServiceDto> services = c.getServices().stream().map(s -> {
                ServiceDto serviceDto = new ServiceDto();
                serviceDto.setServiceId(s.getServiceId());
                serviceDto.setServiceName(s.getServiceName());
                serviceDto.setServiceDescription(s.getServiceDescription());
                serviceDto.setServiceImage(s.getImageUrl());
                serviceDto.setServicePrice(s.getServicePrice());
                return serviceDto;
            }).toList();

            category.setServices(services);
            return category;
        }).toList();

        return mappedCategories;
    }}