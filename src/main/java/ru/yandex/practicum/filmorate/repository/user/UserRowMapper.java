package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
         .id(rs.getInt(1))
         .login(rs.getString(2))
         .email(rs.getString(3))
         .name(rs.getString(4))
         .birthday(rs.getTimestamp(5).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
         .build();


    }

}
