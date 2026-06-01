package backend.dto.requests;

import backend.enums.ETransactionFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedTransactionRequestDto {

    private long userId;

    private int categoryId;

    private double amount;

    private String description;

    private ETransactionFrequency frequency;

    private LocalDate upcomingDate;
}