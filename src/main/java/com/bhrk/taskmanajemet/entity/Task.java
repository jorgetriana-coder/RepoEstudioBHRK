package com.bhrk.taskmanajemet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private Boolean isDone;

    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
