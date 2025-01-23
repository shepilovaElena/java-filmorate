package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Collection<Film> getAllFilms() {

        String query1 = "SELECT film_id, name, description, release_date, duration, rating_id FROM films";

        List<Film> filmsList = jdbcOperations.query(query1,filmRowMapper);

        String query2 = "SELECT genre_id, film_id FROM film_genres_list";

        Map<Integer, List<Genre>> resultMap = new HashMap<>();

        jdbcOperations.query(query2, rs -> {
            int genreId = rs.getInt("genre_id");
            int filmId = rs.getInt("film_id");
            resultMap.computeIfAbsent(filmId, k -> new ArrayList<>()).add(Genre.builder().id(genreId).build());
        });

        for (Film film : filmsList) {
            film.setGenres(resultMap.get(film.getId()));
        }

        return filmsList;
    }

    @Override
    public Film postFilm(Film film) throws ValidationException {

        checkMpa(film.getMpa());
        checkGenre(film.getGenres());

        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (:name, :description, :releaseDate, :duration, :ratingId)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", film.getName());
        parameterSource.addValue("description", film.getDescription());
        parameterSource.addValue("releaseDate", new Timestamp(film.getReleaseDate().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant().getLong(ChronoField.INSTANT_SECONDS)));
        parameterSource.addValue("duration", film.getDuration());
        parameterSource.addValue("ratingId", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sqlQuery, parameterSource, keyHolder, new String[] {"film_id"});

        film.setId(keyHolder.getKey().intValue());

        String sqlQuery1 = "INSERT INTO film_genres_list (genre_id, film_id) VALUES (:genreId, :filmId)";

        List<Map<String, Object>> genreBatchValues = new ArrayList<>();

        List<Genre> genres = film.getGenres();

        for (Genre genre : genres) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("genreId", genre.getId());
            paramMap.put("filmId", film.getId());
            genreBatchValues.add(paramMap);
        }

    jdbcOperations.batchUpdate(sqlQuery1, genreBatchValues.toArray(new Map[0]));

        return film;
    }

    @Override
    public Film putFilm(Film film) {
        checkMpa(film.getMpa());
        checkGenre(film.getGenres());

        String updateQuery = "UPDATE films SET name = :name, description = :description, release_date = :releaseDate, " +
                "duration = :duration, rating_id = :mpaId WHERE film_id = :id";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("name", film.getName());
        param.addValue("description", film.getDescription());
        param.addValue("releaseDate", new Timestamp(film.getReleaseDate().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant().getLong(ChronoField.INSTANT_SECONDS)));
        param.addValue("duration", film.getDuration());
        param.addValue("mpaId", film.getMpa().getId());
        param.addValue("id", film.getId());

        jdbcOperations.update(updateQuery, param);

        String deleteFilmGenresQuery = "DELETE FROM film_genres_list WHERE film_id = :id";

        jdbcOperations.update(deleteFilmGenresQuery, param);

        String addGenresQuery = "INSERT INTO film_genres_list (film_id, genre_id) VALUES (:filmId, :genreId)";

 /// выделить в отдельный метод
        List<Map<String, Object>> genreBatchValues = new ArrayList<>();

        List<Genre> genres = film.getGenres();

        for (Genre genre : genres) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("genreId", genre.getId());
            paramMap.put("filmId", film.getId());
            genreBatchValues.add(paramMap);
        }

        jdbcOperations.batchUpdate(addGenresQuery, genreBatchValues.toArray(new Map[0]));


        return film;
    }

    @Override
    public Film getFilmById(int id) {

        String sqlQuery1 = "SELECT film_id, name, description, release_date, duration, rating_id,  FROM films WHERE film_id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        Film film = jdbcOperations.queryForObject(sqlQuery1, param, filmRowMapper);

        String sqlQuery2 = "SELECT genre_id FROM film_genres_list WHERE film_id = :id";

        List<Integer> filmGenresId = jdbcOperations.queryForList(sqlQuery2, param, Integer.class);

        List<Genre> filmGenres = new ArrayList<>();

        if (!filmGenresId.isEmpty()) {
            for (int i : filmGenresId) {
            filmGenres.add(Genre.builder().id(i).build());
        }}

        film.setGenres(filmGenres);
      return film;
    }


    private void checkMpa(Mpa mpa) {
        String checkMpaQuery = "SELECT EXISTS (SELECT 1 FROM rating WHERE rating_id = :id)";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", mpa.getId());

        if (jdbcOperations.queryForObject(checkMpaQuery, param, Integer.class) == 0) {
            throw new ValidationException("Рейтинга с id " + mpa.getId() + " не существует.");
        }
    }

    private void checkGenre(List<Genre> genresList) {
        String checkMpaQuery = "SELECT genre_id FROM genres";
        List<Integer> genresIdList =  jdbcOperations.queryForList(checkMpaQuery, new HashMap<>(), Integer.class);

        for (Genre genre : genresList) {
            if (!genresIdList.contains(genre.getId())) {
                throw new ValidationException("Жанра с id " + genre.getId() + " не существует.");
            }
        }
    }

}
