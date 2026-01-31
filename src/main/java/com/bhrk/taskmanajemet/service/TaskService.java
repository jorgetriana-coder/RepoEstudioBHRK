package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface TaskService {
    TaskResponseDTO create(TaskRequestDTO taskRequestDTO, Integer userId);

    Page<TaskResponseDTO> findAll(Integer userIdm, Pageable pageable);

    TaskResponseDTO findById(Integer userId, Integer taskId);

    TaskResponseDTO upDateTask(Integer userId, TaskRequestDTO task, Integer taskId);

    void deleteByID(Integer userId, Integer taskId);
}
