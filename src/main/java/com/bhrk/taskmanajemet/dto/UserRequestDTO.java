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
    @Column(nullable = false)
    private String name;

    @NotNull(message = "This field is required")
    @Email(message = "The email format is invalid")
    private String email;

    @NotNull(message = "This field is required")
    @Size(min = 8, message = "Password should have at least 8 characters.")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "This field is required")
    @Past(message = "Birth Date should be in the past")
    @Column(nullable = false)
    private LocalDate birthDate;
}
