package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.dto.*;
import com.bhrk.taskmanajemet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userSaved = userService.create(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @GetMapping("/users")
    ResponseEntity<Page<UserResponseDTO>> findAllUsers(@PageableDefault Pageable pageable) {
        Page<UserResponseDTO> usersFound = userService.findAllUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(usersFound);
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<UserResponseDTO> findUser(@PathVariable Integer userId) {
        UserResponseDTO userFound = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userFound);
    }

    @PutMapping("/users/{userId}")
    ResponseEntity<UserResponseDTO> updateInfoUser(
            @Valid
            @PathVariable Integer userId,
            @RequestBody UserInfoRequestDTO userInfo) {
        UserResponseDTO userUpDate = userService.updateUser(userId, userInfo);
        return ResponseEntity.status(HttpStatus.OK).body(userUpDate);
    }

    @PatchMapping("/users/{userId}")
    ResponseEntity<UserChangePasswordResponseDTO> updateInfoUser(
            @PathVariable Integer userId,
            @Valid
            @RequestBody UserChangePasswordDTO userPassword) throws BadRequestException {
        UserChangePasswordResponseDTO userPassWordUpDate = userService.updatePassword(userId, userPassword);
        return ResponseEntity.status(HttpStatus.OK).body(userPassWordUpDate);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
