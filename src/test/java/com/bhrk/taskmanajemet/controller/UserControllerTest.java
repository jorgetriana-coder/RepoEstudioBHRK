package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.MockFactory.MockFactory;
import com.bhrk.taskmanajemet.dto.*;
import com.bhrk.taskmanajemet.handler.GlobalExceptionHandler;
import com.bhrk.taskmanajemet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                )
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        //given
        UserRequestDTO userRequest = MockFactory.buildUserRequestDto();
        UserResponseDTO userResponse = MockFactory.buildUserResponse();

        //when
        when(service.create(any(UserRequestDTO.class))).thenReturn(userResponse);
        //then
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Jorge"))
                .andExpect(jsonPath("$.email").value("jorge@ejemplo.com"));

        verify(service).create(any(UserRequestDTO.class));
    }

    @Test
    void shouldReturn400WhenNameIsNull() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name(null)
                .email("jorge@ejemplo.com")
                .password("password123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("name: This field is required"));

        verify(service, never()).create(any());
    }

    @Test
    void shouldReturn400WhenNameIsTooShort() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("abc")
                .email("jorge@ejemplo.com")
                .password("password123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("name: Name should have at least 5 characters."));

        verify(service, never()).create(any());
    }
    @Test
    void shouldReturn400WhenEmailIsNull() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email(null)
                .password("password123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("email: This field is required"));

        verify(service, never()).create(any());
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email("correo-invalido")
                .password("password123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("email: The email format is invalid"));

        verify(service, never()).create(any());
    }
    @Test
    void shouldReturn400WhenPasswordIsNull() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password(null)
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("password: This field is required"));

        verify(service, never()).create(any());
    }

    @Test
    void shouldReturn400WhenPasswordTooShort() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("12345")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("password: Password should have at least 8 characters."));

        verify(service, never()).create(any());
    }
    @Test
    void shouldReturn400WhenBirthDateIsNull() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("password123")
                .birthDate(null)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("birthDate: This field is required"));

        verify(service, never()).create(any());
    }
    @Test
    void shouldReturn400WhenBirthDateIsInFuture() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .password("password123")
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("birthDate: Birth Date should be in the past"));

        verify(service, never()).create(any());
    }

    @Test
    void shouldReturnPagedUsers() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 2);

        UserResponseDTO user1 = UserResponseDTO.builder()
                .id(1)
                .name("Jorge")
                .email("jorge@ejemplo.com")
                .build();

        UserResponseDTO user2 = UserResponseDTO.builder()
                .id(2)
                .name("Juan")
                .email("juan@ejemplo.com")
                .build();

        Page<UserResponseDTO> page = new PageImpl<>(
                List.of(user1, user2),
                pageable,
                2
        );

        when(service.findAllUser(any(Pageable.class)))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].email").value("jorge@ejemplo.com"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(service).findAllUser(any(Pageable.class));
    }

    @Test
    void testGetUserById() throws Exception{
        //given
        int userId = 1 ;
        UserResponseDTO user = MockFactory.buildUserResponse();
        //when
        when(service.findById(userId)).thenReturn(user);
        //then
        mockMvc.perform(
                        get("/users/{userId}",userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Jorge"))
                .andExpect(jsonPath("$.email").value("jorge@ejemplo.com"));
        verify(service).findById(anyInt());
    }
    @Test
    void testUpDateUser() throws Exception{
        //given
        int userId = 1 ;
        UserResponseDTO userResponse = MockFactory.buildUserResponse();
        //when
        when(service.updateUser(anyInt(),any(UserInfoRequestDTO.class))).thenReturn(userResponse);
        //then
        mockMvc.perform(
                put("/users/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Jorge"))
                .andExpect(jsonPath("$.email").value("jorge@ejemplo.com"));
        verify(service).updateUser(anyInt(),any(UserInfoRequestDTO.class));

    }

    @Test
    void testUpDatePassword() throws Exception{
        //given
        int userId = 1 ;
        UserChangePasswordResponseDTO userResponse = MockFactory.buildUserChangePasswordResponseDTO();
        UserChangePasswordDTO dto = MockFactory.buildChangePassword();
        //when
        when(service.updatePassword(anyInt(),any(UserChangePasswordDTO.class))).thenReturn(userResponse);
        //then
        mockMvc.perform(
                        patch("/users/{userId}",userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password change accepted"));
        verify(service).updatePassword(anyInt(),any(UserChangePasswordDTO.class));

    }

    @Test
    void shouldUpdatePasswordWhenPasswordNull() throws Exception {
        int userId = 1;

        UserChangePasswordDTO request = UserChangePasswordDTO.builder()
                .password(null)
                .newPassword("newpassword123")
                .build();

        mockMvc.perform(
                        patch("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("password: This field is required"));

        verifyNoInteractions(service);
    }

    @Test
    void shouldUpdatePasswordWhenPasswordShort() throws Exception {
        int userId = 1;

        UserChangePasswordDTO request = UserChangePasswordDTO.builder()
                .password("123")
                .newPassword("newpassword123")
                .build();

        mockMvc.perform(
                        patch("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("password: Password should have at least 8 characters."));

        verifyNoInteractions(service);
    }

    @Test
    void shouldUpdatePasswordWhenNewPasswordNull() throws Exception {
        int userId = 1;

        UserChangePasswordDTO request = UserChangePasswordDTO.builder()
                .password("oldpassword123")
                .newPassword(null)
                .build();

        mockMvc.perform(
                        patch("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("newPassword: This field is required"));

        verifyNoInteractions(service);
    }

    @Test
    void shouldUpdatePasswordWhenNewPasswordShort() throws Exception {
        int userId = 1;

        UserChangePasswordDTO request = UserChangePasswordDTO.builder()
                .password("oldpassword123")
                .newPassword("123")
                .build();

        mockMvc.perform(
                        patch("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("newPassword: The new password should have at least 8 characters."));

        verifyNoInteractions(service);
    }

    @Test
    void testDeleteUser() throws Exception {
        //given
        int userId = 1;
        //when
        doNothing().when(service).deleteUser(anyInt());
        //then
        mockMvc.perform(
                delete("/users/{userId}",userId)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(service).deleteUser(userId);
    }

}