package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
class UserServiceImpTest {

    @Autowired
    private UserService userService;

    UserDto userDto = new UserDto(1L, "Fedor","fedor@mail.ru" );

    @Test
    void createUserDto() {

        UserDto result = userService.createUserDto(userDto);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getUserById() {

        UserDto result = userService.getUserById(userDto.getId());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void update() {

        UserDto result = userService.createUserDto(userDto);
        UserDto upResult = new UserDto(2L, "Symkin", "symkin@mail.ru");
        userService.update(upResult, result.getId());
        assertThat(upResult.getId(), equalTo(result.getId()));
        assertThat(upResult.getName(), equalTo("Вася"));
        assertThat(upResult.getEmail(), equalTo("test@email.com"));

    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUserByIdForCreate() {
    }
}