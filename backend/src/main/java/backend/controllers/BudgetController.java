package backend.controllers;

import backend.dto.requests.BudgetRequest;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;

import backend.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> createBudget(
            @RequestBody BudgetRequest budgetRequest
    ) throws UserNotFoundException, UserServiceLogicException {

        return budgetService.createBudget(budgetRequest);
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getBudgetByMonth(
            @RequestParam("userId") long userId,
            @RequestParam("month") int month,
            @RequestParam("year") long year
    ) throws UserServiceLogicException {

        return budgetService.getBudgetByMonth(userId, month, year);
    }
}