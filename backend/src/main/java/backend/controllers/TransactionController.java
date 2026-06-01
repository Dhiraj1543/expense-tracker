package backend.controllers;

import backend.dto.requests.TransactionRequestDto;
import backend.dto.responses.ApiResponseDto;
import backend.exceptions.CategoryNotFoundException;
import backend.exceptions.TransactionNotFoundException;
import backend.exceptions.TransactionServiceLogicException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.*;
import backend.services.TransactionService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> getAllTransactions(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("searchKey") String searchKey
    ) throws TransactionServiceLogicException {

        return transactionService.getAllTransactions(
                pageNumber,
                pageSize,
                searchKey
        );
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> addTransaction(
            @RequestBody @Valid TransactionRequestDto transactionRequestDto
    ) throws UserNotFoundException,
            CategoryNotFoundException,
            TransactionServiceLogicException {

        return transactionService.addTransaction(transactionRequestDto);
    }

    @GetMapping("/getByUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getTransactionsByUser(
            @RequestParam("email") String email,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("searchKey") String searchKey,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDirec") String sortDirec,
            @RequestParam("transactionType") String transactionType
    ) throws UserNotFoundException,
            TransactionServiceLogicException {

        return transactionService.getTransactionsByUser(
                email,
                pageNumber,
                pageSize,
                searchKey,
                sortField,
                sortDirec,
                transactionType
        );
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getTransactionById(
            @RequestParam("id") Long id
    ) throws TransactionNotFoundException {

        return transactionService.getTransactionById(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> updateTransaction(
            @RequestParam("transactionId") Long transactionId,
            @RequestBody @Valid TransactionRequestDto transactionRequestDto
    ) throws UserNotFoundException,
            CategoryNotFoundException,
            TransactionNotFoundException,
            TransactionServiceLogicException {

        return transactionService.updateTransaction(
                transactionId,
                transactionRequestDto
        );
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> deleteTransaction(
            @RequestParam("transactionId") Long transactionId
    ) throws TransactionNotFoundException,
            TransactionServiceLogicException {

        return transactionService.deleteTransaction(transactionId);
    }
}