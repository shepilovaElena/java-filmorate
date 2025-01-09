package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private int counter = 0;
    private Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film postFilm(Film film) throws ValidationException {
        long nextId = getNextId();
        film.setId(nextId);
        films.put(nextId,film);
        log.info("Фильм успешно добавлен.");
        return film;
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }

    public Film putFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Фильм с id {} не существует", film.getId());
            throw new NotFoundException("Фильм с таким id не найден.");
        }
    }

    private long getNextId() {
        counter = counter + 1;
        return counter;
    }

}
