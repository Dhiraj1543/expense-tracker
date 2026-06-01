package backend.services;

import backend.exceptions.TransactionTypeNotFoundException;
import backend.models.TransactionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionTypeService {

    List<TransactionType> getAllTransactions();

    boolean existsByTransactionTypeId(int transactionTypeId);

    TransactionType getTransactionById(int transactionTypeId)
            throws TransactionTypeNotFoundException;
}