package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.MockFactory.MockFactory;
import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.handler.GlobalExceptionHandler;
import com.bhrk.taskmanajemet.service.TaskService;
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
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                )
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void testCreateTask() throws Exception {
        //given
        int userId = 1;
        TaskRequestDTO taskRequest = MockFactory.buildTaskRequestDto();
        TaskResponseDTO taskResponse = MockFactory.buildTaskResponseDto();

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
    void shouldReturnPagedUsers() throws Exception {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 2);

        TaskResponseDTO task1 = MockFactory.buildTaskResponseDto();

        TaskResponseDTO task2 = TaskResponseDTO.builder()
                .id(1)
                .description("documentacion de la API")
                .isDone(false)
                .targetDate(LocalDate.of(2026,12,12))
                .userId(1)
                .build();

        Page<TaskResponseDTO> page = new PageImpl<>(
                List.of(task1, task2),
                pageable,
                2
        );

        when(service.findAll(anyInt(),any(Pageable.class)))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/users/{userId}/task",userId)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].description").value("documentacion de la API"))
                .andExpect(jsonPath("$.content[1].isDone").value(false))
                .andExpect(jsonPath("$.content[1].targetDate").value("2026-12-12"))
                .andExpect(jsonPath("$.content[1].userId").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(service).findAll(anyInt(),any(Pageable.class));
    }
    @Test
    void shouldReturn400WhenDescriptionIsNull() throws Exception {
        int userId = 1;
        TaskRequestDTO request = TaskRequestDTO.builder()
                .description(null)
                .targetDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/users/{userId}/task",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("description: This field is required"));
    }

    @Test
    void shouldReturn400WhenDescriptionIsEmpty() throws Exception {
        int userId = 1;

        TaskRequestDTO request = TaskRequestDTO.builder()
                .description("")
                .targetDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/users/{userId}/task", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("description: Description should have at least 5 letters."));
    }

    @Test
    void shouldReturn400WhenDescriptionTooShort() throws Exception {
        int userId = 1;

        TaskRequestDTO request = TaskRequestDTO.builder()
                .description("abc")
                .targetDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/users/{userId}/task", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("description: Description should have at least 5 letters."));
    }
    @Test
    void shouldReturn400WhenTargetDateIsNull() throws Exception {
        int userId = 1;

        TaskRequestDTO request = TaskRequestDTO.builder()
                .description("Valid description")
                .targetDate(null)
                .build();

        mockMvc.perform(post("/users/{userId}/task", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("targetDate: This field is required"));
    }

    @Test
    void shouldReturn400WhenTargetDateIsPast() throws Exception {
        int userId = 1;

        TaskRequestDTO request = TaskRequestDTO.builder()
                .description("Valid description")
                .targetDate(LocalDate.now().minusDays(1))
                .build();

        mockMvc.perform(post("/users/{userId}/task", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("targetDate: The date should be in the future."));
    }

    @Test
    void shouldReturn400WhenTargetDateIsToday() throws Exception {
        int userId = 1;

        TaskRequestDTO request = TaskRequestDTO.builder()
                .description("Valid description")
                .targetDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/users/{userId}/task", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("targetDate: The date should be in the future."));
    }

    @Test
    void testGetTaskById() throws Exception{
        //given
        int userId = 1 ;
        int taskId = 1 ;
        TaskResponseDTO task = MockFactory.buildTaskResponseDto();
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
        TaskResponseDTO taskResponse = MockFactory.buildTaskResponseDto();
        TaskRequestDTO taskRequest = MockFactory.buildTaskRequestDto();
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
        TaskResponseDTO task = MockFactory.buildTaskResponseDto();
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
