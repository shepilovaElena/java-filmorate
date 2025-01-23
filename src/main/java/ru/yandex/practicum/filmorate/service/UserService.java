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
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.sql.PreparedStatement;
import java.util.*;


@Service
public class UserService {
    private final UserRepository userDbStorage;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public UserService(UserRepository dbUserStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = dbUserStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User postUser(UserDto userDto) {
        return userDbStorage.postUser(UserMapper.toModel(userDto));
    }

    public User putUser(UserDto userDto) {
        checkUserId(userDto.getId());
        return userDbStorage.putUser(UserMapper.toModel(userDto));
    }

    public Collection<UserDto> getAllUsers() {
        return userDbStorage.getAllUsers().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto getUserById(int id) {
          return UserMapper.toDto(userDbStorage.getUserById(id));
    }

    public String addToFriends(int user1, int user2) { /// сделать проверку на повторы друзей
        checkUserId(user1);
        checkUserId(user2);

        String sqlQuery = "INSERT INTO friendship (user_id, user_friend_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"friendship_id"});
                stmt.setInt(1, user2);
                stmt.setInt(2, user1);
                return stmt;
            }, keyHolder);

        return "friendshipId = " + keyHolder.getKey().intValue();
    }


    public void deleteFromFriends(int user1, int user2) {
       checkUserId(user1);
       checkUserId(user2);

        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND user_friend_id = ?";

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery);
            stmt.setInt(1, user2);
            stmt.setInt(2, user1);
            return stmt;
        });
    }

    public List<User> getJointFriends(int user1Id, int user2Id) {
        checkUserId(user1Id);
        checkUserId(user2Id);

        List<User> user1FriendsList = getUserFriendsList(user1Id);
        List<User> user2FriendsList = getUserFriendsList(user2Id);

        List<User> jointFriendsList = new ArrayList<>();

            for (User user : user2FriendsList) {
                for (User user1 : user1FriendsList) {
                    if (user1.getId() == user.getId()) {
                        jointFriendsList.add(user);
                    }
                }
            }

        return jointFriendsList;
    }

    public List<User> getUserFriendsList(int id) {
        checkUserId(id);
        String sqlQuery = "SELECT user_id FROM friendship WHERE user_friend_id = ?";

            List<User> friends = jdbcTemplate.queryForList(sqlQuery, Integer.class, id)
                    .stream()
                    .map(userDbStorage::getUserById)
                    .toList();
            return friends;
    }

    private void checkUserId(int id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
      if(jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 0) {
          throw new NotFoundException("Пользователь с id " + id + " не найден.");
      }
    }
}
