package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {
    Collection<Film> getAllFilms();

    Film postFilm(Film film) throws ValidationException;

    Film putFilm(Film film);

    Film getFilmById(int id);

   // void deleteFilmById(int id);
}
