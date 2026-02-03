package com.bhrk.taskmanajemet.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "This field is required")
    @Size(min = 5, message = "Name should have at least 5 characters.")
    private String name;

    @NotNull(message = "This field is required")
    @Email(message = "The email format is invalid")
    private String email;

    @NotNull(message = "This field is required")
    @Size(min = 8, message = "Password should have at least 8 characters.")
    private String password;

    @NotNull(message = "This field is required")
    @Past(message = "Birth Date should be in the past")
    private LocalDate birthDate;
}
