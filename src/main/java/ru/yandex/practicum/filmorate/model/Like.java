package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Like {
    private int like_id;
    private int user_id;
    private int film_id;
}
