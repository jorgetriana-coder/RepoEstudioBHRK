package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.MockFactory.MockFactory;
import com.bhrk.taskmanajemet.dto.UserInfoRequestDTO;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import com.bhrk.taskmanajemet.mapper.UserMapperImpl;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Test
    void shouldCreateUser() {
        // given
        UserRequestDTO request = MockFactory.buildUserRequestDto();
        UserResponseDTO expectedResponse = MockFactory.buildUserResponse();

        when(repository.existsByEmail(request.getEmail())).thenReturn(false);
        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1);
                    return user;
                });

        // when
        UserResponseDTO result = userService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedResponse.getId());
        assertThat(result.getName()).isEqualTo(expectedResponse.getName());
        assertThat(result.getEmail()).isEqualTo(expectedResponse.getEmail());

        verify(repository).existsByEmail(request.getEmail());
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldCreateUserWhenEmailExist() {
        //given
        UserRequestDTO mockUserRequest = MockFactory.buildUserRequestDto();
        //when
        when(repository.existsByEmail(Mockito.eq(mockUserRequest.getEmail()))).thenReturn(true);
        //then
        var error = assertThrows(ResourceDuplicateException.class, () -> userService.create(mockUserRequest));
        assertEquals("El email ya está registrado", error.getMessage());
        verify(repository).existsByEmail(mockUserRequest.getEmail());
    }


    @Test
    void shouldFindAll() {
        // given
        Pageable pageable = PageRequest.of(0, 2);

        User user1 = MockFactory.buildUser();
        User user2 = MockFactory.buildUser();

        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(repository.findAll(pageable)).thenReturn(userPage);

        // when
        Page<UserResponseDTO> results = userService.findAllUser(pageable);
        var result = results.toList().getFirst();

        // then
        assertThat(result).isNotNull();
        assertThat(result)
                .extracting(UserResponseDTO::getEmail,
                        UserResponseDTO::getName)
                .containsExactly(
                        user1.getEmail(),
                        user1.getName()
                );

        verify(repository).findAll(pageable);
    }

    @Test
    void shouldFindById() {
        //given
        Integer id = 1;
        User user = MockFactory.buildUser();
        //when
        when(repository.findById(id)).thenReturn(Optional.ofNullable(user));
        //then
        UserResponseDTO result = userService.findById(id);
        assertEquals(1, result.getId());
        assertEquals("jorge@ejemplo.com", result.getEmail());
        assertEquals("Jorge", result.getName());

        verify(repository).findById(id);
    }

    @Test
    void shouldFindByIdWhenIdNotExist() {
        //given
        Integer id = 5;
        //when
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class, () -> userService.findById(id));
        assertEquals("User not found", error.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void shouldUpdate(){
        // given
        Integer userId = 1;

        User existingUser = MockFactory.buildUser();
        existingUser.setId(userId);
        existingUser.setName("Jorge");
        existingUser.setEmail("jorge@ejemplo.com");

        UserInfoRequestDTO updateRequest = UserInfoRequestDTO.builder()
                .name("Jorge Updated")
                .email("jorge@ejemplo.com")
                .build();

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(repository.save(existingUser)).thenReturn(existingUser);

        // when
        UserResponseDTO result = userService.updateUser(userId, updateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Jorge Updated");
        assertThat(result.getEmail()).isEqualTo("jorge@ejemplo.com");

        verify(repository).findById(userId);
        verify(repository).save(existingUser);
    }

    @Test
    void shouldUpdateWhenUserNotExist() {
        //given
        Integer id = 5;
        UserInfoRequestDTO userRequest = MockFactory.buildUserInfoRequestDTO();
        //when
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class, () -> userService.updateUser(id, userRequest));
        assertEquals("User not found", error.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void shouldDeleteUser() {
        Integer id = 1;
        userService.deleteUser(id);

        verify(repository, times(1)).deleteById(id);
    }

}
