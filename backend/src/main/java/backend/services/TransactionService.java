package backend.services;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.TransactionRequestDto;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.TransactionServiceLogicException;
import backend.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    ResponseEntity<ApiResponseDto<?>> addTransaction(
            TransactionRequestDto transactionRequestDto
    ) throws UserNotFoundException,
            CategoryNotFoundException,
            TransactionServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getTransactionById(
            Long transactionId
    ) throws TransactionNotFoundException;

    ResponseEntity<ApiResponseDto<?>> updateTransaction(
            Long transactionId,
            TransactionRequestDto transactionRequestDto
    ) throws TransactionNotFoundException,
            UserNotFoundException,
            CategoryNotFoundException,
            TransactionServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> deleteTransaction(
            Long transactionId
    ) throws TransactionNotFoundException,
            TransactionServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getAllTransactions(
            int pageNumber,
            int pageSize,
            String searchKey
    ) throws TransactionServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getTransactionsByUser(
            String email,
            int pageNumber,
            int pageSize,
            String searchKey,
            String sortField,
            String sortDirection,
            String transactionType
    ) throws UserNotFoundException,
            TransactionServiceLogicException;
}