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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper mapper;
    Pageable pageable = PageRequest.of(0,10);


    @Override
    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO, Integer userId){
        User userFound = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User dont Exist"));
        Task entity = mapper.toEntity(taskRequestDTO);
        entity.setUser(userFound);
        Task taskSaved = taskRepository.save(entity);
        return mapper.toResponse(taskSaved);
    }

    @Override
    public Page<TaskResponseDTO> findAll(Integer userId,Pageable pageable){
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User dont Exist."));
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(mapper::toResponse);
    }

    @Override
    public TaskResponseDTO findById(Integer userId, Integer taskId) {
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User dont Exist."));
        Task taskFound = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task not found"));
        return mapper.toResponse(taskFound);
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
