package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    HashMap<Long, String> emails = new HashMap<>();

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUserDto(UserDto userDto) {
        if (userDto.getEmail() == null) throw new ValidationException("email должен быть указан");
        if (emails.containsValue(userDto.getEmail()) || !userDto.getEmail().contains("@"))
            throw new ValidationException("Пользователь с таким email уже существует");
        userDto = userMapper.toUserDto(userRepository.createUser(userMapper.toUser(userDto)));
        if (userDto.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        emails.put(userDto.getId(), userDto.getEmail());
        return userDto;
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.toUserDto(userRepository.getUserById(id).orElseThrow(() -> new NotFoundException(
                "Пользователь c ID - " + id + " не найден")));
    }

    @Override
    public UserDto update(UserDto newUserDto, Long id) {
        UserDto oldUserDto = getUserById(id);
        if (newUserDto.getEmail() != null) {
            emails.remove(id);
            if (emails.containsValue(newUserDto.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            } else if (newUserDto.getEmail().contains("@")) {
                oldUserDto.setEmail(newUserDto.getEmail());
            } else {
                throw new ValidationException("Неверный email");
            }
        }
        if (newUserDto.getName() != null) oldUserDto.setName(newUserDto.getName());
        oldUserDto.setId(id);
        newUserDto = userMapper.toUserDto(userRepository.updateUser(userMapper.toUser(oldUserDto)));
        emails.put(id, newUserDto.getEmail());
        return newUserDto;
    }

    @Override
    public void deleteUser(long id) {
        emails.remove(id);
        userRepository.deleteUser(id);
    }
}
