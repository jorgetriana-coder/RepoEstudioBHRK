package com.bhrk.taskmanajemet.mapper;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskRequestDTO taskRequestDTO);

    @Mapping(source = "user.id", target = "userId")
    TaskResponseDTO toResponse(Task task);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(TaskRequestDTO taskRequestDTO, @MappingTarget Task task);
}
