package ru.practicum.shareit.user.model;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),user.getName(),user.getEmail());
    }

}
