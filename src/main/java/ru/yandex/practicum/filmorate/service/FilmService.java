package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }


    public Collection<Film> getAllFilms() {
            return filmRepository.getAllFilms();
    }

    public Film postFilm(Film film) throws ValidationException {
         return filmRepository.postFilm(film);
    }

    public Film putFilm(Film film) {
        return filmRepository.putFilm(film);
    }

    public Film getFilmById(int id) {
        return filmRepository.getFilmById(id);
    }

    public void addLike(int filmId, int userId) {
        filmRepository.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmRepository.deleteLike(filmId, userId);
    }

    public List<Film> getBestFilms(String count) {
         return filmRepository.getBestFilms(count);
    }



}
