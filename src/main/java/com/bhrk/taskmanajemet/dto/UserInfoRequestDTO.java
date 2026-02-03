package com.bhrk.taskmanajemet.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDTO {

    @NotNull(message = "This field is required")
    @Size(min = 5, message = "Password should have at least 8 characters.")
    private String name;

    @NotNull(message = "This field is required")
    @Email(message = "The email format is invalid")
    private String email;


    @NotNull(message = "This field is required")
    @Past(message = "Birth Date should be in the past")
    private LocalDate birthDate;

}
