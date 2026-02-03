package com.bhrk.taskmanajemet.service;

import com.bhrk.taskmanajemet.dto.*;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO);

    Page<UserResponseDTO> findAllUser(Pageable pageable);

    UserResponseDTO findById(Integer userId);

    UserResponseDTO updateUser(Integer userId, UserInfoRequestDTO Userinfo);

    void deleteUser(Integer userId);

    UserChangePasswordResponseDTO updatePassword(@Valid Integer userId, UserChangePasswordDTO userPassword) throws BadRequestException;
}
