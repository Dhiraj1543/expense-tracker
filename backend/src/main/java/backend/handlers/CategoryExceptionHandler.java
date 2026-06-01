package backend.handlers;

import backend.dto.responses.ApiResponseDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.CategoryAlreadyExistsException;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.CategoryServiceLogicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>>
    categoryNotFoundExceptionHandler(CategoryNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(CategoryServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<String>>
    categoryServiceLogicExceptionHandler(CategoryServiceLogicException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<String>>
    categoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.CONFLICT,
                        exception.getMessage()
                ));
    }
}