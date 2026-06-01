package backend.services;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.SavedTransactionRequestDto;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import org.springframework.http.ResponseEntity;

public interface SavedTransactionService {

    ResponseEntity<ApiResponseDto<?>> createSavedTransaction(
            SavedTransactionRequestDto requestDto
    ) throws UserServiceLogicException, UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> addSavedTransaction(
            long savedTransactionId
    ) throws UserServiceLogicException, TransactionNotFoundException;

    ResponseEntity<ApiResponseDto<?>> editSavedTransaction(
            long savedTransactionId,
            SavedTransactionRequestDto requestDto
    ) throws UserServiceLogicException, TransactionNotFoundException;

    ResponseEntity<ApiResponseDto<?>> deleteSavedTransaction(
            long savedTransactionId
    ) throws UserServiceLogicException, TransactionNotFoundException;

    ResponseEntity<ApiResponseDto<?>> skipSavedTransaction(
            long savedTransactionId
    ) throws UserServiceLogicException, TransactionNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getAllTransactionsByUser(
            long userId
    ) throws UserServiceLogicException, UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getAllTransactionsByUserAndMonth(
            long userId
    ) throws UserServiceLogicException, UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getSavedTransactionById(
            long savedTransactionId
    ) throws UserServiceLogicException, TransactionNotFoundException;
}