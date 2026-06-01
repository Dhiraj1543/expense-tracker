package backend.dataSeeders;

import backend.enums.ETransactionType;
import backend.models.TransactionType;
import backend.repository.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class TransactionTypeDataSeeder {

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @EventListener
    @Transactional
    public void loadTransactionTypes(ContextRefreshedEvent event) {

        List<ETransactionType> transactionTypes =
                Arrays.stream(ETransactionType.values()).toList();

        for (ETransactionType transactionType : transactionTypes) {

            if (!transactionTypeRepository
                    .existsByTransactionTypeName(transactionType)) {

                transactionTypeRepository
                        .save(new TransactionType(transactionType));
            }
        }
    }
}