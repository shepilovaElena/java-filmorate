package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    FilmStorage inMemoryFilmStorage;
    UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public UserStorage getUserStorage() {
        return inMemoryUserStorage;
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film postFilm(Film film) throws ValidationException {
        return inMemoryFilmStorage.postFilm(film);
    }

    public Film putFilm(Film film) {
        return inMemoryFilmStorage.putFilm(film);
    }

    public Film getFilmById(long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public void addLike(long filmId, long userId) {
            inMemoryFilmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (inMemoryFilmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            inMemoryFilmStorage.getFilmById(filmId).getLikes().remove(userId);
        }
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

       return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(new FilmLikesComparator())
                .limit(countInt)
                .toList();
    }
}
