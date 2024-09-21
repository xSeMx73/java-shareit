package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Optional;


@Getter
@Slf4j
@Repository
public class UserRepository {

  HashMap<Long,User> users = new HashMap<>();

    public User createUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || !users.containsKey(id)) throw new NotFoundException("Пользователь с таким id не существует");
        return Optional.ofNullable(users.get(id));
    }


    public User updateUser(User user,Long id) {
        User user1 = getUserById(id).orElse(user);
        if (user.getEmail() != null && user.getEmail().contains("@")) user1.setEmail(user.getEmail());
        if (user.getName() != null) user1.setName(user.getName());
        users.replace(id, user);
        return user;
    }

    public void deleteUser(long id) {
        users.remove(id);
    }
}
