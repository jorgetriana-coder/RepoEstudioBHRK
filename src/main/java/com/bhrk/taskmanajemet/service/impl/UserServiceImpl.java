package com.bhrk.taskmanajemet.service.impl;

import com.bhrk.taskmanajemet.dto.UserRequestDTO;
import com.bhrk.taskmanajemet.dto.UserResponseDTO;
import com.bhrk.taskmanajemet.entity.User;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.mapper.UserMapper;
import com.bhrk.taskmanajemet.repository.UserRepository;
import com.bhrk.taskmanajemet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) throws ResourceDuplicateException {
        if (repository.existsByEmail((userRequestDTO.getEmail()))) {
            throw new ResourceDuplicateException("El email ya está registrado");
        }
        User entity = mapper.toEntity(userRequestDTO);
        String passwordHash = encoder.encode(entity.getPassword());
        entity.setPassword(passwordHash);
        User save = repository.save(entity);
        return mapper.toResponse(save);
    }

    @Override
    public List<UserResponseDTO> findAllUser() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserResponseDTO findById(Integer userId) {
        User userEntity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        return mapper.toResponse(userEntity);
    }
    @Override
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO) {
        User userEntity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        mapper.update(userRequestDTO,userEntity);
        String passwordHash = encoder.encode(userEntity.getPassword());
        userEntity.setPassword(passwordHash);

        return mapper.toResponse(repository.save(userEntity));
    }

    @Override
    public void deleteUser(Integer userId) {
        repository.deleteById(userId);
    }

}
