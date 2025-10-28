package milestone.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(example = "admin", description = "Username of the user")
    private String username;

    @Schema(example = "admin", description = "Password of the user")
    private String password;
}
