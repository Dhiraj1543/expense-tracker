package backend.controllers;

import backend.models.TransactionType;

import backend.services.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mywallet/transactiontype")
public class TransactionTypeController {

    @Autowired
    private TransactionTypeService transactionTypeService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<TransactionType> getAllTransactionTypes() {

        return transactionTypeService.getAllTransactions();
    }
}