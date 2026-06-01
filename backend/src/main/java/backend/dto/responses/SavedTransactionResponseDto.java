package backend.dto.responses;

import backend.enums.ETransactionFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedTransactionResponseDto {

    private long planId;

    private int transactionType;

    private String categoryName;

    private double amount;

    private String description;

    private ETransactionFrequency frequency;

    private String dueInformation;
}