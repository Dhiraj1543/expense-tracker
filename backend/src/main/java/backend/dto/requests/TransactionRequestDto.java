package backend.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequestDto {

    @Email(message = "Invalid email format!")
    @NotBlank(message = "User email is required!")
    private String userEmail;

    @NotNull(message = "Category id is required!")
    private Integer categoryId;

    @NotBlank(message = "Description is required!")
    @Size(max = 50,
            message = "Description can have at most 50 characters!")
    private String description;

    @NotNull(message = "Amount is required!")
    private Double amount;

    @NotNull(message = "Date is required!")
    private LocalDate date;
}