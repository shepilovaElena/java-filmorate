package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = User.builder()
         .id(rs.getInt(1))
         .login(rs.getString(2))
         .email(rs.getString(3))
         .name(rs.getString(4))
         .birthday(rs.getTimestamp(5).toInstant())
         .build();

       return user;
    }

}
