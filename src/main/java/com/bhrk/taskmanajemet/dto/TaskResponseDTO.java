package com.bhrk.taskmanajemet.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private Integer id;

    private String description;

    private Boolean isDone = false;

    private LocalDate targetDate;

    private Integer userId;

}