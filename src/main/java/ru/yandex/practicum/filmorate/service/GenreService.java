package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final NamedParameterJdbcOperations jdbcOperations;

    public GenreService(GenreRepository genreRepository, NamedParameterJdbcOperations jdbcOperations) {
        this.genreRepository = genreRepository;
        this.jdbcOperations = jdbcOperations;
    }

    public Genre getGenreById(int id) {
        checkId(id);
        return genreRepository.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    private void checkId(int id) {
        String checkGenreIdQuery = "SELECT EXISTS (SELECT 1 FROM genres WHERE genre_id = :id)";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        if (jdbcOperations.queryForObject(checkGenreIdQuery, param, Integer.class) == 0) {
            throw new NotFoundException("Жанр с id " + id + " не существует.");
        }
    }

}
