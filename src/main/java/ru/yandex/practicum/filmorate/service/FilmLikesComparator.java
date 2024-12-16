package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmLikesComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        if (film1.getLikes().isEmpty() && film2.getLikes().isEmpty()) {
            return 0;
        }
        if (film1.getLikes().isEmpty()) {
            return 1;
        }
        if (film2.getLikes().isEmpty()) {
            return -1;
        }
        return film2.getLikes().size() - film1.getLikes().size();
    }
}
