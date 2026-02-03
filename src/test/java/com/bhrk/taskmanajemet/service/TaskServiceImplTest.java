package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.MockFactory.MockFactory;
import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.mapper.TaskMapperImpl;
import com.bhrk.taskmanajemet.repository.TaskRepository;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({TaskMapperImpl.class})
@ExtendWith(SpringExtension.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskMapperImpl taskMapper;

    private UserRepository userRepository;

    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.taskRepository = mock(TaskRepository.class);
        this.taskService = new TaskServiceImpl(taskRepository,userRepository,taskMapper);
    }

    @Test
    void shouldCreateTask(){
        //given
        int id = 1;
        Task mockTask = MockFactory.buildTask();
        TaskResponseDTO mockTaskResponse = MockFactory.buildTaskResponseDto();
        TaskRequestDTO mockTaskRequest = MockFactory.buildTaskRequestDto();
        User user = MockFactory.buildUser();
        //when
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        //then
        TaskResponseDTO response = taskService.create(mockTaskRequest,id);
        assertThat(response).extracting(
                TaskResponseDTO::getId,
                TaskResponseDTO::getDescription,
                TaskResponseDTO::getIsDone,
                TaskResponseDTO::getTargetDate
        ).containsExactly(
                mockTaskResponse.getId(),
                mockTaskResponse.getDescription(),
                mockTaskResponse.getIsDone(),
                mockTaskResponse.getTargetDate()
        );
        verify(userRepository).findById(id);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldCreateTaskWhenUserIdNotExist(){
        //given
        int id = 5;
        TaskRequestDTO task = MockFactory.buildTaskRequestDto();
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> taskService.create(task,id));
        assertEquals("User dont Exist",error.getMessage());
        verify(userRepository).findById(id);
    }

    @Test
    void shouldFindAllTasksByUserId() {
        // given
        Integer userId = 1;
        Pageable pageable = PageRequest.of(0, 2);

        User user = MockFactory.buildUser();
        user.setId(userId);

        Task task1 = MockFactory.buildTask();
        Task task2 = MockFactory.buildTask();

        Page<Task> taskPage = new PageImpl<>(
                List.of(task1, task2),
                pageable,
                2
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findAll(pageable))
                .thenReturn(taskPage);

        // when
        Page<TaskResponseDTO> results = taskService.findAll(userId,pageable);

        var result = results.toList().getFirst();

        // then
        assertThat(result).isNotNull();
        assertThat(results.getSize()).isEqualTo(2);

        assertThat(result)
                .extracting(
                        TaskResponseDTO::getId,
                        TaskResponseDTO::getDescription,
                        TaskResponseDTO::getIsDone,
                        TaskResponseDTO::getTargetDate
                        )
                .containsExactly(
                        task1.getId(),
                        task1.getDescription(),
                        task1.getIsDone(),
                        task1.getTargetDate()
                );

        verify(userRepository).findById(userId);
        verify(taskRepository).findAll(pageable);

    }
    @Test
    void shouldFindAllWhenUserNOtExist(){
        //given
        int id = 5;
        Pageable pageable = PageRequest.of(0, 2);
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> taskService.findAll(id,pageable));
        assertEquals("User dont Exist.",error.getMessage());
        verify(userRepository).findById(id);
    }
    @Test
    void shouldFindById() {
        // given
        int userId = 1;
        int taskId = 1;

        User user = MockFactory.buildUser();
        user.setId(userId);

        Task task = MockFactory.buildTask();
        task.setId(taskId);
        task.setUser(user); // SOLO para el mapper

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        // when
        TaskResponseDTO result = taskService.findById(userId, taskId);

        // then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Aprobar modelo de UX/UI figma", result.getDescription());
        assertEquals(userId, result.getUserId());

        verify(userRepository).findById(userId);
        verify(taskRepository).findById(taskId);

    }
    @Test
    void shouldFindByIdWhenIdUserNotExist() {
        //given
        Integer userId = 5;
        Integer taskId = 5;
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class, () -> taskService.findById(userId,taskId));
        assertEquals("User dont Exist.", error.getMessage());
        verify(userRepository).findById(userId);
    }
    @Test
    void shouldFindByIdWhenIdTaskNotExist() {
        //given
        Integer userId = 5;
        Integer taskId = 5;
        User user = MockFactory.buildUser();
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class, () -> taskService.findById(userId,taskId));
        assertEquals("Task not found", error.getMessage());
        verify(userRepository).findById(userId);
        verify(taskRepository).findById(taskId);
    }

    @Test
    void shouldUpdateTask(){
        // Arrange
        int userId = 1;
        int taskId = 1;
        User user = MockFactory.buildUser();
        TaskRequestDTO updateRequest = TaskRequestDTO.builder()
                .description("Descripción actualizada por Juan")
                .isDone(true)
                .targetDate(LocalDate.of(2026,8,12))
                .build();
        Task existingTask = MockFactory.buildTask();
        Task updatedTask = Task.builder()
                .id(taskId)
                .description("Descripción actualizada por Juan")
                .isDone(true)
                .targetDate(existingTask.getTargetDate())
                .build();

        TaskResponseDTO expectedResponse = TaskResponseDTO.builder()
                .id(taskId)
                .description("Descripción actualizada por Juan")
                .isDone(true)
                .userId(1)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponseDTO result = taskService.upDateTask(userId,updateRequest,taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Descripción actualizada por Juan", result.getDescription());

        verify(taskRepository).save(any(Task.class));
    }
    @Test
    void shouldUpdateWhenUserNOtExist(){
        //given
        int userId = 1;
        int taskId = 1;
        TaskRequestDTO task = MockFactory.buildTaskRequestDto();
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> taskService.upDateTask(userId,task,taskId));
        assertEquals("User dont Exist.",error.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldUpdateWhenTaskNOtExist(){
        //given
        int userId = 1;
        int taskId = 1;
        TaskRequestDTO task = MockFactory.buildTaskRequestDto();
        User user = MockFactory.buildUser();
        //when
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        var error = assertThrows(NotFoundException.class,() -> taskService.upDateTask(userId,task,taskId));
        assertEquals("Task not found",error.getMessage());
        verify(taskRepository).findById(taskId);
    }


    @Test
    void shouldDeleteTask(){
        //given
        int userId = 1;
        int taskId = 1;
        //when
        taskService.deleteByID(userId,taskId);
        //then
        verify(taskRepository,times(1)).deleteById(taskId);
    }

}
