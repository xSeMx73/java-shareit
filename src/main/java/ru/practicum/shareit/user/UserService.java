package ru.practicum.shareit.user;

public interface UserService {

    User createUser(User user);

   User getUserById(long id);

    User update(User user, Long id);

    void deleteUser(long id);
}
