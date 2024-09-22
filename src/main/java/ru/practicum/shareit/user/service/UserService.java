package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {

    User createUser(User user);

    User getUserById(long id);

    User update(User user, Long id);

    void deleteUser(long id);
}
