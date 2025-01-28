package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Primary
@Repository
@RequiredArgsConstructor
public class UserDbRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;


    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT user_id, login, email, name, birthday FROM users";
        List<User> userList = jdbcTemplate.query(sqlQuery, userRowMapper);
        return userList;
    }

    @Override
    public User postUser(User user) {
        String sqlQuery = "INSERT INTO users (login, email, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setTimestamp(4, new Timestamp(user.getBirthday().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant().getLong(ChronoField.INSTANT_SECONDS)));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User putUser(User user) {
        checkUserId(user.getId());
        String sqlQuery = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setTimestamp(4, new Timestamp(user.getBirthday().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant().getLong(ChronoField.INSTANT_SECONDS)));
            stmt.setInt(5, user.getId());
            return stmt;
        });

        return user;
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT user_id, login, email, name, birthday FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, userRowMapper, id);
    }


   @Override
    public void addToFriends(int user1Id, int user2Id) { /// сделать проверку на повторы друзей
        checkUserId(user1Id);
        checkUserId(user2Id);

        String sqlQuery = "INSERT INTO friendship (user_id, user_friend_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"friendship_id"});
            stmt.setInt(1, user2Id);
            stmt.setInt(2, user1Id);
            return stmt;
        }, keyHolder);

    }

    @Override
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

    @Override
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

    @Override
    public List<User> getUserFriendsList(int id) {
        checkUserId(id);
        String sqlQuery = "SELECT user_id FROM friendship WHERE user_friend_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id)
                .stream()
                .map(i -> getUserById(i))
                .toList();
    }

    private void checkUserId(int id) {

        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";

      if (jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 0) {
          throw new NotFoundException("Пользователь с id " + id + " не найден.");
      }
    }
}
