package spring.tuto.flowdesk.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.tuto.flowdesk.dto.CategoryDtoSimplified;
import spring.tuto.flowdesk.dto.ListServicesDto;
import spring.tuto.flowdesk.dto.ServiceDto;
import spring.tuto.flowdesk.dto.ServiceDtoSummary;
import spring.tuto.flowdesk.entities.Category;
import spring.tuto.flowdesk.exceptions.ApiException;
import spring.tuto.flowdesk.exceptions.ResponseStructure;
import spring.tuto.flowdesk.exceptions.RessourceNotFoundException;
import spring.tuto.flowdesk.repositories.CategoryRepository;
import spring.tuto.flowdesk.repositories.ServiceRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ServiceEntityServiceImplt implements ServiceEntityService{


    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    CategoryRepository categoryRepository;


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ServiceRepository serviceRepository;


    private static final Logger log = LoggerFactory.getLogger(ServiceEntityServiceImplt.class);


    @Override
    @Transactional
    public ServiceDto createService(Long categoryId, ServiceDto serviceDto, MultipartFile image) throws IOException {

        if(categoryId == null || categoryId<=0){

            throw new ApiException("Invalid Category");
        }
        Category checkCategoryIfExist = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new RessourceNotFoundException("Category","categoryId",categoryId)
        );
        if( image == null || image.isEmpty()){
            throw new ApiException("Must enter an image to continue");

        }

        String imageUrl = cloudinaryService.uploadImage(image);

        spring.tuto.flowdesk.entities.Service newService = new spring.tuto.flowdesk.entities.Service();

        newService.setServiceName(serviceDto.getServiceName());
        newService.setServicePrice(serviceDto.getServicePrice());
        newService.setServiceDescription(serviceDto.getServiceDescription());
        newService.setImageUrl(imageUrl);
        newService.setCategory(checkCategoryIfExist);

        checkCategoryIfExist.getServices().add(newService);

        categoryRepository.save(checkCategoryIfExist);

        spring.tuto.flowdesk.entities.Service savedService = checkCategoryIfExist.getServices().get(checkCategoryIfExist.getServices().size()-1);

        return modelMapper.map(savedService,ServiceDto.class);






    }

    @Override
    public ListServicesDto getAllServices(Integer pageNumber, Integer pageSize, String sortBy, String sortMethod,Long categoryId,String keyword) {

        Sort sort = sortMethod.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();


        Pageable page = PageRequest.of(pageNumber,pageSize,sort);


        Page<spring.tuto.flowdesk.entities.Service> servicePage ;
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            // Both keyword and category filter
            servicePage = serviceRepository.findByServiceNameContainingIgnoreCaseAndCategoryCategoryId(keyword.trim(), categoryId, page);
        } else if (hasKeyword) {
            // Only keyword filter
            servicePage = serviceRepository.findByServiceNameContainingIgnoreCase(keyword.trim(), page);
        } else if (hasCategory) {
            // Only category filter
            servicePage = serviceRepository.findByCategoryCategoryId(categoryId, page);
        } else {
            // No filters - get all
            servicePage = serviceRepository.findAll(page);
        }



        List<spring.tuto.flowdesk.entities.Service> serviceList = servicePage.getContent();


        ListServicesDto responseServices = new ListServicesDto();

        responseServices.setPageNumber(servicePage.getNumber());
        responseServices.setPageSize(servicePage.getSize());
        responseServices.setLastPage(servicePage.isLast());
        responseServices.setTotalPage(servicePage.getTotalPages());
        responseServices.setTotalElements(servicePage.getTotalElements());

        List<ServiceDto> services = serviceList.stream().map(
                s ->{

                  ServiceDto serviceDto =  modelMapper.map(s,ServiceDto.class);

                    CategoryDtoSimplified category = modelMapper.map(s.getCategory(),CategoryDtoSimplified.class);

                    serviceDto.setCategory(category);

                    return serviceDto;
                }
        ).toList();


        responseServices.setServices(services);

        return responseServices;





}

    @Override
    public ServiceDtoSummary updateService(Long serviceId, MultipartFile image, String serviceName, String description, Double price) throws IOException {

        spring.tuto.flowdesk.entities.Service updatedServiceIfExist = serviceRepository.findById(serviceId).orElseThrow(
                ()-> new RessourceNotFoundException("Service","ServiceId",serviceId)
        );

        if(serviceName != null){

            updatedServiceIfExist.setServiceName(serviceName);
        }if(description != null){
            updatedServiceIfExist.setServiceDescription(description);
        }if(price != null){
            updatedServiceIfExist.setServicePrice(price);
        }if(image != null && !image.isEmpty()){
            cloudinaryService.deleteImage(updatedServiceIfExist.getImageUrl());

            String newImageUrl = cloudinaryService.uploadImage(image);

            updatedServiceIfExist.setImageUrl(newImageUrl);
        }

        spring.tuto.flowdesk.entities.Service updatedOne = serviceRepository.save(updatedServiceIfExist);


        return modelMapper.map(updatedOne, ServiceDtoSummary.class);


    }

    @Override
    @Transactional
    public ResponseStructure deleteService(Long serviceId) throws IOException {

        spring.tuto.flowdesk.entities.Service deletedOne = serviceRepository.findById(serviceId).orElseThrow(
                ()-> new RessourceNotFoundException("Service","ServiceId",serviceId)
        );

        Category checkCategory = deletedOne.getCategory();


        if(checkCategory != null && checkCategory.getServices() != null){

            checkCategory.getServices().remove(deletedOne);
        }


        if(deletedOne.getImageUrl() != null){

            cloudinaryService.deleteImage(deletedOne.getImageUrl());
        }


        log.debug("check if the controller reach this before delete");



        serviceRepository.delete(deletedOne);

        log.debug("check if the controller reach this after delete");



        ResponseStructure map = new ResponseStructure("Service "+serviceId+" has been deleted successfully",true);

        return map;



    }


}
