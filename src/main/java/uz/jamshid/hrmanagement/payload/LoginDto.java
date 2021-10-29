package uz.jamshid.hrmanagement.payload;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotNull
    @Email
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;
}
