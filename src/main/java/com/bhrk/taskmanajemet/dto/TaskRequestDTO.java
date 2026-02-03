package com.bhrk.taskmanajemet.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @Size(min = 5, message = "Description should have at least 5 letters.")
    @NotNull(message = "This field is required")
    private String description;

    @Builder.Default
    private Boolean isDone = false;

    @Future(message = "The date should be in the future.")
    @NotNull(message = "This field is required")
    private LocalDate targetDate;
}
