package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов.");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на добавление фильма.");
        checkFilmReleaseDate(film);
        return filmService.postFilm(film);
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        log.info("Получен запрос на изменение фильма с id {}.", film.getId());
        checkFilmReleaseDate(film);
        return filmService.putFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        if (!checkId(id)) {
            throw new NotFoundException("Фильм с id" + id + " не найден.");
        }
        if (!filmService.getUserStorage().getAllUsers().contains(filmService.getUserStorage().getUserById(userId))) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден.");
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        if (!checkId(id)) {
            throw new NotFoundException("Фильм с id" + id + " не найден.");
        }
        if (!filmService.getUserStorage().getAllUsers().contains(filmService.getUserStorage().getUserById(userId))) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден.");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getBestFilms(count);
    }

    private void checkFilmReleaseDate(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
    }

    private boolean checkId(long id) {
        boolean check = true;
        Optional<Long> userId = filmService.getAllFilms().stream()
                .map(Film::getId)
                .filter(i -> i == id)
                .findFirst();
        if (userId.isEmpty()) {
            check = false;
        }
        return check;
    }
}
