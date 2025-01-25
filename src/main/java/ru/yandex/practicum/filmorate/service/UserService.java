package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;


@Service
public class UserService {
    private final UserRepository userDbRepository;

    @Autowired
    public UserService(UserRepository dbUserStorage) {
        this.userDbRepository = dbUserStorage;
    }

    public User postUser(UserDto userDto) {
        return userDbRepository.postUser(UserMapper.toModel(userDto));
    }

    public User putUser(UserDto userDto) {
        return userDbRepository.putUser(UserMapper.toModel(userDto));
    }

    public Collection<UserDto> getAllUsers() {
        return userDbRepository.getAllUsers().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto getUserById(int id) {
          return UserMapper.toDto(userDbRepository.getUserById(id));
    }

    public void addToFriends(int user1Id, int user2Id) {
        userDbRepository.addToFriends(user1Id, user2Id);
    }

    public void  deleteFromFriends(int user1Id, int user2Id) {
        userDbRepository.deleteFromFriends(user1Id, user2Id);
    }

    public List<User> getJointFriends(int user1Id, int user2Id) {
        return userDbRepository.getJointFriends(user1Id, user2Id);
    }

    public List<User> getUserFriendsList(int id) {
        return userDbRepository.getUserFriendsList(id);
    }

}
