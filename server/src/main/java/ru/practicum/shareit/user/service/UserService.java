package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

public interface UserService {

    UserDto createUserDto(UserDto userDto);

    UserDto getUserById(long id);

    UserDto update(UserDto userDto, Long id);

    void deleteUser(long id);

    UserDto getUserByIdForCreate(Long userId);
}
