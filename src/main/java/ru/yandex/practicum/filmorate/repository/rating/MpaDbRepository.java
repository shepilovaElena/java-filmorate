package ru.yandex.practicum.filmorate.repository.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class MpaDbRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Mpa getRatingById(int id) {
        String getRatingQuery = "SELECT rating_name FROM rating WHERE rating_id = :id";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        return new Mpa(id, jdbcOperations.queryForObject(getRatingQuery, param, String.class));
    }

    @Override
    public List<Mpa> getAllRatings() {
        String getAllRatingsQuery = "SELECT rating_id, rating_name  FROM rating";
        return jdbcOperations.query(getAllRatingsQuery, mpaRowMapper);
    }
}
