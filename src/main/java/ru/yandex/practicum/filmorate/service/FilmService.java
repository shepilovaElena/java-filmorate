package ru.yandex.practicum.filmorate.service;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.util.*;

@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final NamedParameterJdbcOperations jdbcOperations;

    public FilmService (FilmRepository filmRepository, NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.filmRepository = filmRepository;
    }


    public Collection<Film> getAllFilms() {
            return filmRepository.getAllFilms();
    }

    public Film postFilm(Film film) throws ValidationException {
         return filmRepository.postFilm(film);
    }

    public Film putFilm(Film film) {
        checkId("films", "film_id", film.getId());
        return filmRepository.putFilm(film);
    }

    public Film getFilmById(int id) {
        checkId("films", "film_id", id);
        return filmRepository.getFilmById(id);
    }

    public void addLike(int filmId, int userId) {
        String addLikeQuery = "INSERT INTO likes (film_id, user_id) VALUES (:filmId, :userId)";
        checkId("films", "film_id", filmId);
        checkId("users", "user_id", userId);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("filmId", filmId);
        param.addValue("userId", userId);

        jdbcOperations.update(addLikeQuery, param, keyHolder, new String[] {"like_id"});
    }

    public void deleteLike(int filmId, int userId) {
        checkId("films", "film_id", filmId);
        checkId("users", "user_id", userId);
        checkLike(filmId, userId);

        String deleteQuery = "DELETE FROM likes WHERE film_id = :filmId AND user_id = :userId";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("filmId", filmId);
        param.addValue("userId", userId);

        jdbcOperations.update(deleteQuery, param);
    }

    public List<Film> getBestFilms(String count) {

        int countInt;
        try {
           countInt = Integer.parseInt(count);
        } catch (Exception e) {
            throw  new ValidationException("Передано не число, а " + count);
        }
        if (countInt <= 0) {
            throw new ValidationException("Количество фильмов должно быть больше нуля");
        }

        String countQuery = "SELECT COUNT(film_id) AS amount_likes, film_id FROM likes " +
                "GROUP BY film_id ORDER BY amount_likes DESC";

        Map<Integer, Integer> resultMap = new LinkedHashMap<>();

             jdbcOperations.query(countQuery, rs -> {
             int amountLikes = rs.getInt("amount_likes");
             int filmId = rs.getInt("film_id");
             resultMap.put(filmId, amountLikes);
         });

        return resultMap.keySet().stream()
                .map(film_id -> getFilmById(film_id))
                .limit(Long.parseLong(count))
                .toList();
    }

    private void checkId(String tableName, String columnName, int id) {
        String query = String.format("SELECT EXISTS(SELECT 1 FROM %s WHERE %s = :id)", tableName, columnName);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
    try {
        if (jdbcOperations.queryForObject(query, parameterSource, Integer.class) == 0 ) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
    } catch (DataAccessException e) {
        e.getCause().printStackTrace();
    }

    }

    private void checkLike(int filmId, int userId) {
        String checkLikeQuery = String.format("SELECT EXISTS (SELECT 1 FROM likes) WHERE %s = :filmId AND %s = :userId",
                filmId, userId);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("filmId", filmId);
        param.addValue("userId", userId);

        if (jdbcOperations.queryForObject(checkLikeQuery, param, Integer.class) == 0) {
            throw new NotFoundException("Лайк от пользователя с id " + userId + " фильму с id " + filmId + " не найден.");
        }
    }

}
