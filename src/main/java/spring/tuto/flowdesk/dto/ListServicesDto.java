package spring.tuto.flowdesk.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListServicesDto {


    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean lastPage;

    private List<ServiceDto> services = new ArrayList<>();
}
