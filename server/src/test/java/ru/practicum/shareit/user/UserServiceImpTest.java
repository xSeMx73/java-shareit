package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class UserServiceImpTest {

    @Autowired
    private UserService userService;

    UserDto userDto = new UserDto(1L, "Fedor", "fedor@mail.ru");

    @Test
    void createUserDto() {

        UserDto result = userService.createUserDto(userDto);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getUserById() {

        UserDto temp = userService.createUserDto(userDto);
        UserDto result = userService.getUserById(temp.getId());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void update() {

        UserDto result = userService.createUserDto(userDto);
        UserDto upResult = new UserDto(2L, "Symkin", "symkin@mail.ru");
        result = userService.update(upResult, result.getId());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo("Symkin"));
        assertThat(result.getEmail(), equalTo("symkin@mail.ru"));

    }

    @Test
    void deleteUser() {

        UserDto result = userService.createUserDto(userDto);
        UserDto getResult = userService.getUserById(result.getId());
        assertEquals(getResult, result);
        userService.deleteUser(result.getId());
        assertThatThrownBy(() -> userService.getUserById(result.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void getUserByIdForCreate() {

        assertThatThrownBy(() -> userService.getUserByIdForCreate(userDto.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}