package backend.models;

import backend.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_types")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionTypeId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETransactionType transactionTypeName;

    public TransactionType(ETransactionType transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }
}