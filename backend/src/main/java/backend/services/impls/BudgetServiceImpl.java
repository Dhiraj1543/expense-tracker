package backend.services.impls;

import backend.dto.responses.ApiResponseDto;
import backend.dto.requests.BudgetRequest;
import backend.enums.ApiResponseStatus;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.models.Budget;
import backend.models.User;
import backend.repository.BudgetRepository;
import backend.repository.UserRepository;
import backend.services.BudgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponseDto<?>> createBudget(BudgetRequest budgetRequest)
            throws UserNotFoundException, UserServiceLogicException {

        try {

            User user = userRepository.findById(budgetRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User not found with id " + budgetRequest.getUserId()
                    ));

            Budget budget = budgetRepository.findByUserIdAndMonthAndYear(
                    budgetRequest.getUserId(),
                    LocalDate.now().getMonthValue(),
                    LocalDate.now().getYear()
            );

            if (budget == null) {

                budget = new Budget();
                budget.setUser(user);
                budget.setAmount(budgetRequest.getAmount());
                budget.setMonth(LocalDate.now().getMonthValue());
                budget.setYear(LocalDate.now().getYear());

            } else {
                budget.setAmount(budgetRequest.getAmount());
            }

            budgetRepository.save(budget);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Budget created successfully!"
                    ));

        } catch (Exception e) {

            log.error("Failed to create budget: {}", e.getMessage());

            throw new UserServiceLogicException(
                    "Failed to create budget: Try again later!"
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getBudgetByMonth(
            long userId, int month, long year
    ) throws UserServiceLogicException {

        try {

            Budget budget = budgetRepository.findByUserIdAndMonthAndYear(
                    userId, month, year
            );

            double amount = (budget == null) ? 0 : budget.getAmount();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            amount
                    ));

        } catch (Exception e) {

            log.error("Failed to fetch budget amount: {}", e.getMessage());

            throw new UserServiceLogicException(
                    "Failed to fetch budget: Try again later!"
            );
        }
    }
}