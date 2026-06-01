package backend.controllers;

import backend.dto.requests.SavedTransactionRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.UserServiceLogicException;
import backend.exceptions.*;
import backend.services.SavedTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/saved")
public class SavedTransactionController {

    @Autowired
    private SavedTransactionService savedTransactionService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> createSavedTransaction(
            @RequestBody SavedTransactionRequestDto requestDto
    ) throws UserServiceLogicException, UserNotFoundException {

        return savedTransactionService.createSavedTransaction(requestDto);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> addSavedTransaction(
            @RequestParam("id") long id
    ) throws UserServiceLogicException, TransactionNotFoundException {

        return savedTransactionService.addSavedTransaction(id);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> editSavedTransaction(
            @RequestParam("id") long id,
            @RequestBody SavedTransactionRequestDto requestDto
    ) throws UserServiceLogicException, TransactionNotFoundException {

        return savedTransactionService.editSavedTransaction(id, requestDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> deleteSavedTransaction(
            @RequestParam("id") long id
    ) throws UserServiceLogicException, TransactionNotFoundException {

        return savedTransactionService.deleteSavedTransaction(id);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getAllTransactionsByUser(
            @RequestParam("id") long id
    ) throws UserServiceLogicException, UserNotFoundException {

        return savedTransactionService.getAllTransactionsByUser(id);
    }

    @GetMapping("/month")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getAllTransactionsByUserAndMonth(
            @RequestParam("id") long id
    ) throws UserServiceLogicException, UserNotFoundException {

        return savedTransactionService.getAllTransactionsByUserAndMonth(id);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getAllTransactionsById(
            @RequestParam("id") long id
    ) throws UserServiceLogicException, TransactionNotFoundException {

        return savedTransactionService.getSavedTransactionById(id);
    }

    @PostMapping("/skip")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> skipSavedTransaction(
            @RequestParam("id") long id
    ) throws TransactionNotFoundException, UserServiceLogicException {

        return savedTransactionService.skipSavedTransaction(id);
    }
}