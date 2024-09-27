package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Создание пользователя");
        userDto = userService.createUserDto(userDto);
        log.info("Создан пользователь - {}", userDto);
        return userDto;
    }

    @GetMapping("/{userID}")
    public UserDto getUserById(@PathVariable("userID") long id) {
        log.info("Запрошен пользователь с ID - {}", id);
        return userService.getUserById(id);
    }

    @PatchMapping("/{userID}")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userID") long id) {
        log.info("Обновление пользователя {}", userDto);
        userDto = userService.update(userDto, id);
        log.info("Обновлен пользователь - {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable("userID") long id) {
        log.info("Удаление пользователя - {}", id);
        userService.deleteUser(id);
    }

}
