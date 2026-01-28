package com.bhrk.taskmanajemet.MockFactory;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MockitoFactory {

    public static UserRequestDTO buildUserRequestDto() {
        return UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("MysecretPassword123")
                .birthDate(LocalDate.parse("1996-04-05"))
                .build();
    }

    public static UserResponseDTO buildUserResponse() {
        return UserResponseDTO.builder()
            .id(1)
            .name("Jorge")
            .email("jorge@ejemplo.com")
            .build();
    }

    public static List<UserResponseDTO> buildUserResponseList(){
        return new ArrayList<>(List.of(buildUserResponse()));
    }


    public static TaskRequestDTO buildTaskRequestDto() {
        return TaskRequestDTO.builder()
                .description("Aprobar modelo UX/UI figma")
                .isDone(false)
                .targetDate(LocalDate.parse("2026-04-13"))
                .build();
    }

    public static TaskResponseDTO buildTaskResponseDto(){
        return TaskResponseDTO.builder()
                .id(1)
                .description("Aprobar modelo de UX/UI figma")
                .isDone(false)
                .targetDate(LocalDate.parse("2026-04-13"))
                .userId(1)
                .build();

    }

    public static List<TaskResponseDTO> buildTasksResponseList() {
        return new ArrayList<>(List.of(MockitoFactory.buildTaskResponseDto()));
    }

    public static User buildUser() {
        return User.builder()
                .id(1)
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("MysecretPassword123")
                .birthDate(LocalDate.parse("1996-04-05"))
                .build();
    }

    public static List<User> buildUsersList() {
        return new ArrayList<>(List.of(MockitoFactory.buildUser()));
    }

    public static Task buildTask() {
        var user = User.builder()
                .id(1)
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("contraseñaSecreta")
                .build();
        Task task =  Task.builder()
                .id(1)
                .description("Aprobar modelo de UX/UI figma")
                .isDone(false)
                .targetDate(LocalDate.parse("2026-04-13"))
                .user(user)
                .build();
        return task;
    }

    public static List<Task> buildTaskList(){
        return new ArrayList<>(List.of(buildTask()));
    }
}
