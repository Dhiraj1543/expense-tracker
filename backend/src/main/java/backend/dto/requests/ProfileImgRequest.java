package backend.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImgRequest {

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email is required!")
    private String email;

    private MultipartFile file;
}