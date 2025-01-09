package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.util.*;


@Service
public class UserService {
    private final UserStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public UserService(UserStorage dbUserStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = dbUserStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User postUser(UserDto userDto) {

        return userDbStorage.postUser(UserMapper.toModel(userDto));
    }

    public User putUser(UserDto userDto) {

        return userDbStorage.putUser(UserMapper.toModel(userDto));
    }

    public Collection<UserDto> getAllUsers() {
        return userDbStorage.getAllUsers().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public String addToFriends(int userSubmitted, int userReceived) { /// не работает
        checkUserId(userSubmitted);
        checkUserId(userReceived);

        String sqlQuery = "INSERT INTO friendship (user_submitted_request_id, user_received_request_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"friendship_id"});
            stmt.setInt(1, userSubmitted);
            stmt.setInt(2, userReceived);
            return stmt;
        }, keyHolder);

        return "friendshipId = " + keyHolder.getKey().intValue();
    }

    public void deleteFromFriends(int userId, int userfriendId) {
       checkUserId(userId);
       checkUserId(userfriendId);

        String sqlQuery = "DELETE FROM friendship (user_submitted_request_id, user_received_request_id) WHERE ";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setInt(1, userfriendId);
            stmt.setInt(2, userId);
            return stmt;
        }, keyHolder);

    }

    public List<User> getJointFriends(int user1Id, int user2Id) {
        checkUserId(user1Id);
        checkUserId(user2Id);
        return null;
    }

    public List<User> getUserFriendsList(int id) {
        checkUserId(id);
        return null;
    }

    private void checkUserId(int id) {
        String sqlQuery = "SELECT user_id FROM users";
        Optional<Integer> userIdOptional = jdbcTemplate.queryForList(sqlQuery, Integer.class).stream()
                .filter(userId -> id == userId)
                .findFirst();
      if(userIdOptional.isEmpty()) {
          throw new NotFoundException("Пользователь с id" + id + " не найден.");
      }
    }
}
