package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Like {
    private int likeId;
    private int userId;
    private int filmId;
}
