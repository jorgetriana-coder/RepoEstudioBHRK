package com.bhrk.taskmanajemet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDTO {

    @NotNull(message = "This field is required")
    @Size(min = 8, message = "Password should have at least 8 characters.")
    private String password;

    @NotNull(message = "This field is required")
    @Size(min = 8, message = "The new password should have at least 8 characters.")
    private String newPassword;

}
