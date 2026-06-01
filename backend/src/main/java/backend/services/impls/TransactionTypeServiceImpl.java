package backend.services.impls;

import backend.exceptions.TransactionTypeNotFoundException;
import backend.models.TransactionType;
import backend.repository.TransactionTypeRepository;
import backend.services.TransactionTypeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    public List<TransactionType> getAllTransactions() {
        return transactionTypeRepository.findAll();
    }

    @Override
    public boolean existsByTransactionTypeId(int transactionTypeId) {
        return transactionTypeRepository.existsById(transactionTypeId);
    }

    @Override
    public TransactionType getTransactionById(int transactionTypeId)
            throws TransactionTypeNotFoundException {

        return transactionTypeRepository.findById(transactionTypeId)
                .orElseThrow(() ->
                        new TransactionTypeNotFoundException(
                                "Transaction type not found with id "
                                        + transactionTypeId
                        )
                );
    }
}