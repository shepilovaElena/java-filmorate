package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
