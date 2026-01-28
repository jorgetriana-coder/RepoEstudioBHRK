package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.MockFactory.MockitoFactory;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.handler.GlobalExceptionHandler;
import com.bhrk.taskmanajemet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;


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
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        //given
        UserRequestDTO userRequest = MockitoFactory.buildUserRequestDto();
        UserResponseDTO userResponse = MockitoFactory.buildUserResponse();

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
    void testGetUsers() throws Exception{
        //given
        List<UserResponseDTO> users = MockitoFactory.buildUserResponseList();
        //when
        when(service.findAllUser()).thenReturn(users);
        //then
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(users))
            ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Jorge"))
                .andExpect(jsonPath("$[0].email").value("jorge@ejemplo.com"));

        verify(service).findAllUser();
        }
    @Test
    void testGetUserById() throws Exception{
        //given
        int userId = 1 ;
        UserResponseDTO user = MockitoFactory.buildUserResponse();
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
        UserResponseDTO userResponse = MockitoFactory.buildUserResponse();
        UserRequestDTO userRequest = MockitoFactory.buildUserRequestDto();
        //when
        when(service.updateUser(anyInt(),any(UserRequestDTO.class))).thenReturn(userResponse);
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
        verify(service).updateUser(anyInt(),any(UserRequestDTO.class));

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