package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    HashMap<Long, String> emails = new HashMap<>();
    Long id = 0L;
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        if (user.getEmail() == null) throw new InternalServerException("email должен быть указан");
        if (emails.containsValue(user.getEmail()) || !user.getEmail().contains("@"))
            throw new InternalServerException("Пользователь с таким email уже существует");
        user.setId(++id);
        emails.put(user.getId(), user.getEmail());
        userRepository.createUser(user);
        if (user.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        log.info("Пользователь {} создан с id = {}", user.getName(), user.getId());
        return user;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id).orElseThrow(() -> new NotFoundException(
                "Пользователь c ID - " + id + " не найден"));
    }

    @Override
    public User update(User user, Long id) {
        if (userRepository.getUserById(id).isEmpty()) throw new NotFoundException(
                "Пользователь c ID - " + id + " не найден");
        emails.remove(id);
        if (user.getEmail() != null) {
            if (emails.containsValue(user.getEmail())) {
                throw new InternalServerException("Пользователь с таким email уже существует");
            }
        }
        user = userRepository.updateUser(user, id);
        user.setId(id);
        emails.put(id, user.getEmail());
        return user;

    }

    @Override
    public void deleteUser(long id) {
        if (userRepository.getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователь c ID - " + id + " не найден");
        }
        emails.remove(id);
        userRepository.deleteUser(id);
    }
}
