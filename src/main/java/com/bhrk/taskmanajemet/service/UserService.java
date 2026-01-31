package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.dto.UserInfoRequestDTO;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO);

    Page<UserResponseDTO> findAllUser(Pageable pageable);

    UserResponseDTO findById(Integer userId);

    UserResponseDTO updateUser(Integer userId, UserInfoRequestDTO Userinfo);

    void deleteUser(Integer userId);
}
