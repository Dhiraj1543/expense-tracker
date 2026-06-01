package backend.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordRequestDto {

    @NotBlank(message = "Email is required!")
    @Email(message = "Email is not in valid format!")
    private String email;

    private String currentPassword;

    @NotBlank(message = "New password is required!")
    @Size(min = 8, max = 20,
            message = "New password must be between 8 and 20 characters!")
    private String newPassword;

    public ResetPasswordRequestDto(
            String email,
            String currentPassword,
            String newPassword
    ) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public ResetPasswordRequestDto(
            String email,
            String newPassword
    ) {
        this.email = email;
        this.newPassword = newPassword;
    }
}