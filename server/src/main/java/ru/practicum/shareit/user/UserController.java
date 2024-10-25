package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        userDto = userService.createUserDto(userDto);
        log.info("Создан пользователь - {}", userDto);
        return userDto;
    }

    @GetMapping("/{userID}")
    public UserDto getUserById(@PathVariable("userID") long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable("userId") long userId) {
        userDto = userService.update(userDto, userId);
        log.info("Обновлен пользователь - {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
    }

}
