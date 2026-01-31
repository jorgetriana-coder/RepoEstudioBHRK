package com.bhrk.taskmanajemet.mapper;

import com.bhrk.taskmanajemet.dto.UserInfoRequestDTO;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponse(User user);

    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(UserInfoRequestDTO userInfoRequestDTO, @MappingTarget User user);
}
