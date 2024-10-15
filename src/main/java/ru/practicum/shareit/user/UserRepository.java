package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Optional;


@Getter
@Slf4j
@Repository
public class UserRepository {

    Long id = 0L;
    HashMap<Long, User> users = new HashMap<>();

    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    public void deleteUser(long id) {
        users.remove(id);
    }
}
