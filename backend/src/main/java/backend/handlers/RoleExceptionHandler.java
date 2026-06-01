package backend.handlers;

import backend.dto.responses.ApiResponseDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.RoleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoleExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>>
    roleNotFoundExceptionHandler(RoleNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }
}