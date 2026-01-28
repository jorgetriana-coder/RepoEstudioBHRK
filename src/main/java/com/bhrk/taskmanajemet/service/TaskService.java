package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;


import java.util.List;

public interface TaskService {
    TaskResponseDTO create(TaskRequestDTO taskRequestDTO, Integer userId);

    List<TaskResponseDTO> findAll(Integer userId);

    TaskResponseDTO findById(Integer userId, Integer taskId);

    TaskResponseDTO upDateTask(Integer userId, TaskRequestDTO task, Integer taskId);

    void deleteByID(Integer userId, Integer taskId);
}
