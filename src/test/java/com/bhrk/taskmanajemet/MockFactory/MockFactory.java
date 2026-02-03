package com.bhrk.taskmanajemet.MockFactory;

import com.bhrk.taskmanajemet.dto.*;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MockFactory {

    public static UserRequestDTO buildUserRequestDto() {
        return UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("MysecretPassword123")
                .birthDate(LocalDate.of(1996,5,6))
                .build();
    }

    public static UserResponseDTO buildUserResponse() {
        return UserResponseDTO.builder()
            .id(1)
            .name("Jorge")
            .email("jorge@ejemplo.com")
            .build();
    }

    public static UserInfoRequestDTO buildUserInfoRequestDTO(){
        return UserInfoRequestDTO.builder()
                .name("Juan")
                .email("juan@ejemplo")
                .birthDate(LocalDate.of(2000,7,6))
                .build();
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
    public static UserChangePasswordDTO buildChangePassword(){
        return UserChangePasswordDTO.builder()
                .password("12345678")
                .newPassword("87654321")
                .build();
    }
    public static Task buildTask(){
        return Task.builder()
                .id(1)
                .description("Aprobar modelo de UX/UI figma")
                .isDone(false)
                .targetDate(LocalDate.parse("2026-04-13"))
                .build();
    }


    public static UserChangePasswordResponseDTO buildUserChangePasswordResponseDTO() {
        return new UserChangePasswordResponseDTO("Password change accepted");
    }
}
