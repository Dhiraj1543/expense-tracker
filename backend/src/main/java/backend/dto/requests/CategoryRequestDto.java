package backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required!")
    @Size(max = 30,
            message = "Category name cannot have more than 30 characters!")
    private String categoryName;

    @NotNull(message = "Transaction type is required!")
    private Integer transactionTypeId;
}