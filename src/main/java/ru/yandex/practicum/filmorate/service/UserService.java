package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        inMemoryUserStorage.getUserById(userId).getFriends().add(userFriendId);
        inMemoryUserStorage.getUserById(userFriendId).getFriends().add(userId);
    }

    public void deleteFromFriends(long userId, long userFriendId) {
            inMemoryUserStorage.getUserById(userId).getFriends().remove(userFriendId);
            inMemoryUserStorage.getUserById(userFriendId).getFriends().remove(userId);

    }

    public List<User> getJointFriends(long user1Id, long user2Id) {
        return inMemoryUserStorage.getUserById(user1Id).getFriends().stream()
                .filter(id -> inMemoryUserStorage.getUserById(user2Id).getFriends().contains(id))
                .map(id -> inMemoryUserStorage.getUserById(id))
                .collect(Collectors.toList());
    }

    public List<User> getUserFriendsList(long id) {
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .map(friendId -> inMemoryUserStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }
}
