package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import com.bhrk.taskmanajemet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userRequestDTO) throws ResourceDuplicateException {
        UserResponseDTO userSaved = userService.create(userRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(userSaved.getId()).toUri();
        ResponseEntity.created(location).build();
        return ResponseEntity.created(location).body(userSaved);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> usersFound = userService.findAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(usersFound);
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<UserResponseDTO> findUser(@PathVariable Integer userId){
        UserResponseDTO userFound = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userFound);
    }

    @PutMapping("/users/{userId}")
    ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userUpDate = userService.updateUser(userId, userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userUpDate);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
