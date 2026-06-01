package backend.handlers;

import backend.dto.responses.ApiResponseDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.UserVerificationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>>
    userNotFoundExceptionHandler(UserNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>>
    userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.CONFLICT,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UserServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<?>>
    userServiceLogicExceptionHandler(UserServiceLogicException exception) {

        return ResponseEntity
                .badRequest()
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UserVerificationFailedException.class)
    public ResponseEntity<ApiResponseDto<?>>
    userVerificationFailedExceptionHandler(UserVerificationFailedException exception) {

        return ResponseEntity
                .badRequest()
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseDto<?>>
    maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException exception) {

        return ResponseEntity
                .badRequest()
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>>
    methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errorMessages = new ArrayList<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMessages.add(error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        errorMessages.toString()
                ));
    }
}