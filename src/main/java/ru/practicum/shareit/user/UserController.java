package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Создание пользователя");
        user = userService.createUser(user);
        log.info("Создан пользователь - {}", user);
        return user;
    }

    @GetMapping("/{userID}")
    public User getUserById(@PathVariable("userID") long id) {
        log.info("Запрошен пользователь с ID - {}", id);
        return userService.getUserById(id);
    }

    @PatchMapping("/{userID}")
    public User updateUser(@RequestBody User user, @PathVariable("userID") long id) {
        log.info("Обновление пользователя");
        user = userService.update(user, id);
        log.info("Обновлен пользователь - {}", user);
        return user;
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable("userID") long id) {
        log.info("Удаление пользователя - {}", id);
        userService.deleteUser(id);
    }

}
