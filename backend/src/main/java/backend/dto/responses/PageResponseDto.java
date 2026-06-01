package backend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {

    private T data;

    private int totalNoOfPages;

    private Long totalNoOfRecords;
}