package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public UserStorage getUserStorage() {
        return inMemoryUserStorage;
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User postUser(User user) {
        return inMemoryUserStorage.postUser(user);
    }

    public User putUser(User user) {
        return inMemoryUserStorage.putUser(user);
    }

    public void addToFriends(long userId, long userFriendId) {
        checkUserId(userId);
        checkUserId(userFriendId);
        inMemoryUserStorage.getUserById(userId).getFriends().add(userFriendId);
        inMemoryUserStorage.getUserById(userFriendId).getFriends().add(userId);
    }

    public void deleteFromFriends(long userId, long userFriendId) {
       checkUserId(userId);
       checkUserId(userFriendId);

       inMemoryUserStorage.getUserById(userId).getFriends().remove(userFriendId);
       inMemoryUserStorage.getUserById(userFriendId).getFriends().remove(userId);

    }

    public List<User> getJointFriends(long user1Id, long user2Id) {
        checkUserId(user1Id);
        checkUserId(user2Id);

        User user1 = inMemoryUserStorage.getUserById(user1Id);
        User user2 = inMemoryUserStorage.getUserById(user2Id);
        return user1.getFriends().stream()
                .filter(id -> user2.getFriends().contains(id))
                .map(id -> inMemoryUserStorage.getUserById(id))
                .collect(Collectors.toList());
    }

    public List<User> getUserFriendsList(long id) {
        checkUserId(id);
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .map(friendId -> inMemoryUserStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    private void checkUserId(long id) {
        if (inMemoryUserStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id" + id + " не найден.");
        }
    }
}
