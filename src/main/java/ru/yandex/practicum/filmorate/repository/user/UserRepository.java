package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    Collection<User> getAllUsers();

    User postUser(User user);

    User putUser(User user);

    User getUserById(int id);

    void addToFriends(int user1Id, int user2Id);

    void deleteFromFriends(int user1Id, int user2Id);

    List<User> getJointFriends(int user1Id, int user2Id);

    List<User> getUserFriendsList(int id);

}
