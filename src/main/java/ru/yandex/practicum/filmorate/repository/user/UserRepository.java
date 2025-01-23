package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAllUsers();

    User postUser(User user);

    User putUser(User user);

    User getUserById(int id);
}
