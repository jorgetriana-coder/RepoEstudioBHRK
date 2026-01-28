package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.MockFactory.MockitoFactory;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import com.bhrk.taskmanajemet.mapper.UserMapper;
import com.bhrk.taskmanajemet.mapper.UserMapperImpl;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import({UserMapperImpl.class})
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Autowired
    private UserMapperImpl mapper;

    private UserRepository repository;

    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        this.repository = mock(UserRepository.class);
        this.userService = new UserServiceImpl(mapper, repository, new BCryptPasswordEncoder());
    }
//should TDD -mantras del tdd rojo: prueba falla verder: prueba pasa

// probar como funciona encoder, probar cosas rpovenientes de librerias externas no tiene sentido a menos de que el comportamiento interfiera directmaente

    @Test
    void shouldCreateUser() {
        //given
        UserRequestDTO mockUserRequest = MockitoFactory.buildUserRequestDto();
        User mockUser = MockitoFactory.buildUser();
        //when
        when(repository.existsByEmail(Mockito.eq(mockUserRequest.getEmail()))).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(mockUser);
        //then
        UserResponseDTO userCreated = userService.create(mockUserRequest);
        assertEquals(1, userCreated.getId());
        assertEquals("Jorge", userCreated.getName());
        assertEquals("jorge@ejemplo.com", userCreated.getEmail());

        verify(repository).existsByEmail(anyString());
        verify(repository).save(any(User.class));

    }
    @Test
    void shouldCreateUserWhenEmailExist(){
        //given
        UserRequestDTO mockUserRequest = MockitoFactory.buildUserRequestDto();
        //when
        when(repository.existsByEmail(Mockito.eq(mockUserRequest.getEmail()))).thenReturn(true);
        //then
        var error= assertThrows(ResourceDuplicateException.class, () -> userService.create(mockUserRequest));
        assertEquals("El email ya está registrado", error.getMessage());
        verify(repository).existsByEmail(mockUserRequest.getEmail());
    }


    @Test
    void shouldFindAll() {
        //given
        List<User> mockUsers = MockitoFactory.buildUsersList();
        //when
        when(repository.findAll()).thenReturn(mockUsers);
        //then
        List<UserResponseDTO> users = userService.findAllUser();
        assertEquals(1, users.size());
        verify(repository).findAll();
        var responses = users.getFirst();
        assertThat(responses)
                .extracting(
                        UserResponseDTO::getId,
                        UserResponseDTO::getName,
                        UserResponseDTO::getEmail
                ).containsExactly(
                        1,
                        "Jorge",
                        "jorge@ejemplo.com"
                );
    }

    @Test
    void shouldFindById() {
        //given
        Integer id = 1;
        User user = MockitoFactory.buildUser();
        //when
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        //then
        UserResponseDTO userDB = userService.findById(id);
        assertEquals(1, userDB.getId());
        assertEquals("jorge@ejemplo.com", userDB.getEmail());
        assertEquals("Jorge", userDB.getName());

        verify(repository).findById(id);
    }

    @Test
    void shouldFindByIdWhenIdNotExist(){
        //given
        Integer id = 5;
        //when
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> userService.findById(id));
        assertEquals("User not found",error.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void shouldUpdate(){
        //given
        Integer id = 1;
        UserRequestDTO userRequestDTO = MockitoFactory.buildUserRequestDto();
        User mockUser = MockitoFactory.buildUser();

        mockUser.setName(userRequestDTO.getName());
        mockUser.setEmail(userRequestDTO.getEmail());
        mockUser.setPassword(userRequestDTO.getPassword());
        mockUser.setBirthDate(userRequestDTO.getBirthDate());

        //when
        when(repository.findById(1)).thenReturn(Optional.of(mockUser));
        when(repository.save(any(User.class))).thenReturn(mockUser);

        //then
        var response = userService.updateUser(id,userRequestDTO);
        assertThat(response).extracting(
                UserResponseDTO::getId,
                UserResponseDTO::getName,
                UserResponseDTO::getEmail
        ).containsExactly(
                id,
                mockUser.getName(),
                mockUser.getEmail()
        );

        verify(repository).findById(id);
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldUpdateWhenUserNotExist(){
        //given
        Integer id = 5;
        UserRequestDTO userRequest = MockitoFactory.buildUserRequestDto();
        //when
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> userService.updateUser(id,userRequest));
        assertEquals("User not found",error.getMessage());
        verify(repository).findById(id);
    }
    @Test
    void shouldDeleteUser(){
        Integer id = 1;
        userService.deleteUser(id);

        verify(repository,times(1)).deleteById(id);
    }

}
