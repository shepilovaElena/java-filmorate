package ru.yandex.practicum.filmorate.repository.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRepository {

    Mpa getRatingById(int id);

    List<Mpa> getAllRatings();
}
