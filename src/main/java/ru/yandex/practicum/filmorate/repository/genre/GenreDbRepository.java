package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class GenreDbRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private  final GenreRowMapper genreRowMapper;

    @Override
    public Genre getGenreById(int id) {
        String getGenreQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = :id";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        return jdbcOperations.queryForObject(getGenreQuery, param, genreRowMapper);
    }

    @Override
    public List<Genre> getAllGenres() {
        String getAllGenresQuery = "SELECT genre_id, genre_name  FROM genres";
        return jdbcOperations.query(getAllGenresQuery, genreRowMapper);
    }


}
