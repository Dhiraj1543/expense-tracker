package backend.services;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.BudgetRequest;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BudgetService {

    ResponseEntity<ApiResponseDto<?>> createBudget(
            BudgetRequest budgetRequest
    ) throws UserNotFoundException,
            UserServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getBudgetByMonth(
            long userId,
            int month,
            long year
    ) throws UserServiceLogicException;
}