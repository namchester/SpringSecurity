package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginDTO {
    @Email @NotBlank
    private String email;
    @NotBlank @Size(min = 6, max = 100)
    private String password;
}
