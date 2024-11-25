package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int counter = 0;
    private Map<Integer, Film> films = new HashMap<>();

    private int getNextId() {
        if (counter == 0) {
            counter = 1;
        } else {
            counter = counter + 1;
        }
        return counter;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов.");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на добавление фильма.");
        if (!(film.getReleaseDate() == null) && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        int nextId = getNextId();
        film.setId(nextId);
        films.put(nextId,film);
        log.info("Фильм успешно добавлен.");
        return film;
    }

    @PutMapping()
    public Film putFilm(@RequestBody Film film) {
        log.info("Получен запрос на изменение фильма с id {}.", film.getId());
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Фильм с id {} не существует", film.getId());
            throw new ValidationException("Фильм с таким id не найден.");
        }

    }

}
