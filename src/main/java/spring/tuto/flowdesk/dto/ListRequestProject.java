package spring.tuto.flowdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRequestProject {


    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean lastPage;

    List<ProjectRequest> allRequestedProject = new ArrayList<>();
}
