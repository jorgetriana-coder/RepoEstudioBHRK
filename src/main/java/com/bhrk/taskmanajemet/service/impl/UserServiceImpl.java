package com.bhrk.taskmanajemet.service.impl;

import com.bhrk.taskmanajemet.dto.UserInfoRequestDTO;
import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.mapper.UserMapper;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    Pageable pageable = PageRequest.of(0, 10);

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        if (repository.existsByEmail((userRequestDTO.getEmail()))) {
            throw new ResourceDuplicateException("El email ya está registrado");
        }
        User UserEntity = mapper.toEntity(userRequestDTO);
        String passwordHash = encoder.encode(UserEntity.getPassword());
        UserEntity.setPassword(passwordHash);
        User UserSaved = repository.save(UserEntity);
        return mapper.toResponse(UserSaved);
    }

    @Override
    public Page<UserResponseDTO> findAllUser(Pageable pageable) {
        Page<User> Users = repository.findAll(pageable);
        return Users.map(mapper::toResponse);

    }

    @Override
    public UserResponseDTO findById(Integer userId) {
        User userEntity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        return mapper.toResponse(userEntity);
    }

    @Override
    public UserResponseDTO updateUser(Integer userId, UserInfoRequestDTO userInfo) {
        User userEntity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        mapper.update(userInfo, userEntity);
        return mapper.toResponse(repository.save(userEntity));
    }

    @Override
    public void deleteUser(Integer userId) {
        repository.deleteById(userId);
    }

}
