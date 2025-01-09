package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.List;


@Primary
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage{

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
            stmt.setTimestamp(4, new Timestamp(user.getBirthday().getLong(ChronoField.INSTANT_SECONDS)));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User putUser(User user) {
        String sqlQuery = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setTimestamp(4, new Timestamp(user.getBirthday().getLong(ChronoField.INSTANT_SECONDS)));
            stmt.setInt(5, user.getId());
            return stmt;
        });

        return user;
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT user_id, login, email, name, birthday FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, userRowMapper, id);
    }

}
