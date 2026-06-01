package backend.handlers;

import backend.dto.responses.ApiResponseDto;
import backend.enums.ApiResponseStatus;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.TransactionServiceLogicException;
import backend.exceptions.TransactionTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(TransactionServiceLogicException.class)
    public ResponseEntity<ApiResponseDto<String>>
    transactionServiceLogicExceptionHandler(
            TransactionServiceLogicException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>>
    transactionNotFoundExceptionHandler(
            TransactionNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(TransactionTypeNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>>
    transactionTypeNotFoundExceptionHandler(
            TransactionTypeNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        ApiResponseStatus.FAILED,
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }
}