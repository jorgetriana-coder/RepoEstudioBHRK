package com.bhrk.taskmanajemet.mapper;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequestDTO taskRequestDTO);
    @Mapping(source = "user.id", target = "userId")
    TaskResponseDTO toResponse(Task task);

    void update(TaskRequestDTO taskRequestDTO,@MappingTarget  Task task);
}
