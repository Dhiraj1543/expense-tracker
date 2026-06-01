package backend.repository;

import backend.enums.ETransactionType;
import backend.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {

    TransactionType findByTransactionTypeName(ETransactionType transactionTypeName);

    boolean existsByTransactionTypeName(ETransactionType transactionTypeName);
}