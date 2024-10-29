package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUserDto(UserDto userDto) {
        if (!validateEmail(userDto.getEmail()))
            throw new ValidationException("Пользователь с таким email уже существует");
        userDto = userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        if (userDto.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        return userDto;
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new ValidationException(
                "Пользователь c ID - " + id + " не найден")));
    }

    @Override
    public UserDto update(UserDto newUserDto, Long id) {
        UserDto oldUserDto = getUserById(id);
        if (newUserDto.getEmail() != null) {
            if (!validateEmail(newUserDto.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            } else {
                oldUserDto.setEmail(newUserDto.getEmail());
            }
        }
        if (newUserDto.getName() != null) oldUserDto.setName(newUserDto.getName());
        oldUserDto.setId(id);
        newUserDto = userMapper.toUserDto(userRepository.save(userMapper.toUser(oldUserDto)));
        return newUserDto;
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserByIdForCreate(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Пользователь c ID - " + id + " не найден")));
    }

    public Boolean validateEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }
}
