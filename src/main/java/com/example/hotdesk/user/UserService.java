package com.example.hotdesk.user;

import com.example.hotdesk.common.service.GenericCrudService;
import com.example.hotdesk.user.dto.*;
import com.example.hotdesk.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class UserService extends GenericCrudService<User, Integer, UserCreateDto, UserUpdateDto, UserResponseDto, UserPatchDto> {
    private final UserDtoMapper mapper;
    private final UserRepository repository;
    private final Class<User> entityClass = User.class;

    @Override
    protected User save(UserCreateDto createDto) {
        return repository.save(mapper.toEntity(createDto));
    }

    @Override
    protected User updateEntity(UserUpdateDto userUpdateDto, User user) {
        return null;
    }
}