package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO) throws ResourceDuplicateException;

    List<UserResponseDTO> findAllUser();

    UserResponseDTO findById(Integer userId);

    UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO);

    void deleteUser(Integer userId);
}
