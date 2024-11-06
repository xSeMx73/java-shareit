package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Создание пользователя");
        return userClient.createUser(userDto);
    }

    @GetMapping("/{userID}")
    public ResponseEntity<Object> getUserById(@PathVariable("userID") long id) {
        log.info("Запрошен пользователь с ID - {}", id);
        return userClient.getUserById(id);
    }

    @PatchMapping("/{userID}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable("userID") long id) {
        log.info("Обновление пользователя {}", userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable("userID") long id) {
        log.info("Удаление пользователя - {}", id);
        userClient.deleteUser(id);
    }

}
