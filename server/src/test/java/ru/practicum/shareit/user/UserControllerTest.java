package ru.practicum.shareit.user;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();



    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDto = new UserDto(1L, "Fedor", "fedor@mail.ru");
    }

    @Test
    void createUser_ShouldReturnUserDto() throws Exception {

        when(userService.createUserDto(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId())); // Предположим, id - поле в UserDto
    }

    @Test
    void getUserById_ShouldReturnUserDto() throws Exception {
        long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userID}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId())); // Предположим, id - поле в UserDto
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDto() throws Exception {
        long userId = 1L;
        when(userService.update(any(UserDto.class), eq(userId))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId())); // Предположим, id - поле в UserDto
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}