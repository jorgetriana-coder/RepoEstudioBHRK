package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.MockFactory.MockitoFactory;
import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.handler.GlobalExceptionHandler;
import com.bhrk.taskmanajemet.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.reporting.ReportEntry;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private TaskService service;
    @InjectMocks
    private TaskController controller;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void testCreateTask() throws Exception {
        //given
        int userId = 1;
        TaskRequestDTO taskRequest = MockitoFactory.buildTaskRequestDto();
        TaskResponseDTO taskResponse = MockitoFactory.buildTaskResponseDto();

        //when
        when(service.create(any(TaskRequestDTO.class),anyInt())).thenReturn(taskResponse);
        //then
        mockMvc.perform(
                        post("/users/{userId}/task",userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(taskRequest))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Aprobar modelo de UX/UI figma"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.targetDate").value("2026-04-13"))
                .andExpect(jsonPath("$.userId").value(1));

        verify(service).create(any(TaskRequestDTO.class),anyInt());
    }

    @Test
    void testGetTasks() throws Exception{
        //given
        List<TaskResponseDTO> tasks = MockitoFactory.buildTasksResponseList();
        int userId = 1;
        //when
        when(service.findAll(anyInt())).thenReturn(tasks);
        //then
        mockMvc.perform(
                        get("/users/{userId}/task",userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tasks))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].description").value("Aprobar modelo de UX/UI figma"))
                .andExpect(jsonPath("$[0].isDone").value(false))
                .andExpect(jsonPath("$[0].targetDate").value("2026-04-13"))
                .andExpect(jsonPath("$[0].userId").value(1));
        verify(service).findAll(anyInt());
    }

    @Test
    void testGetTaskById() throws Exception{
        //given
        int userId = 1 ;
        int taskId = 1 ;
        TaskResponseDTO task = MockitoFactory.buildTaskResponseDto();
        //when
        when(service.findById(userId,taskId)).thenReturn(task);

        //then
        mockMvc.perform(
                        get("/users/{userId}/task/{taskId}",userId,taskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(task))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Aprobar modelo de UX/UI figma"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.targetDate").value("2026-04-13"))
                .andExpect(jsonPath("$.userId").value(1));

        verify(service).findById(anyInt(),anyInt());
    }

    @Test
    void testUpDateTask() throws Exception{
        //given
        int userId = 1;
        int taskId = 1;
        TaskResponseDTO taskResponse = MockitoFactory.buildTaskResponseDto();
        TaskRequestDTO taskRequest = MockitoFactory.buildTaskRequestDto();
        //when
        when(service.upDateTask(anyInt(),any(TaskRequestDTO.class),anyInt())).thenReturn(taskResponse);
        //then
        mockMvc.perform(
                        put("/users/{userId}/task/{taskId}",userId,taskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(taskResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Aprobar modelo de UX/UI figma"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.targetDate").value("2026-04-13"))
                .andExpect(jsonPath("$.userId").value(1));
        verify(service).upDateTask(anyInt(),any(TaskRequestDTO.class),anyInt());
    }
    @Test
    void testDeleteUser() throws Exception {
        //given
        int userId = 1 ;
        int taskId = 1 ;
        TaskResponseDTO task = MockitoFactory.buildTaskResponseDto();
        //when
        doNothing().when(service).deleteByID(anyInt(),anyInt());
        //then
        mockMvc.perform(
                delete("/users/{userId}/task/{taskId}",userId,taskId)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(service).deleteByID(anyInt(),anyInt());
    }
}
