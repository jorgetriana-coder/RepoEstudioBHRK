package com.bhrk.taskmanajemet.service.impl;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.mapper.TaskMapper;
import com.bhrk.taskmanajemet.repository.TaskRepository;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper mapper;


    @Override
    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO, Integer userId){
        User userFound = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User dont Exist"));
        Task entity = mapper.toEntity(taskRequestDTO);
        entity.setUser(userFound);
        Task task = taskRepository.save(entity);
        TaskResponseDTO taskResponseDTO = mapper.toResponse(task);
        taskResponseDTO.setUserId(userId);
        return taskResponseDTO;
    }

    @Override
    public List<TaskResponseDTO> findAll(Integer userId){
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User dont Exist."));
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponseDTO> tasksList =tasks.stream().map(mapper::toResponse).toList();
        return tasksList;
    }

    @Override
    public TaskResponseDTO findById(Integer userId, Integer taskId) {
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User dont Exist."));
        Task taskFound = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task not found"));
        TaskResponseDTO taskResponse = mapper.toResponse(taskFound);
        return taskResponse;
    }

    @Override
    public TaskResponseDTO upDateTask(Integer userId, TaskRequestDTO task, Integer taskId) {
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User dont Exist."));
        Task taskFound = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task not found"));
        mapper.update(task,taskFound);
        taskRepository.save(taskFound);
        return mapper.toResponse(taskFound);
    }

    @Override
    public void deleteByID(Integer userId, Integer taskId) {
        taskRepository.deleteById(taskId);
    }
}
