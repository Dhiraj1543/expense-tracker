package backend.dto.responses;

import backend.enums.ApiResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {

    private ApiResponseStatus status;

    private HttpStatus httpStatus;

    private T response;
}
