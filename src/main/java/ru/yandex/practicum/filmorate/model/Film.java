package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@Slf4j
public class Film {
    @Builder.Default
    private int id = 0;
    @NotBlank(message = "Поле не должно быть пустым.")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым.")
    @Size(message = "Описание не должно превышать 200 символов.", max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1,message = "Длительность фильма не может быть меньше минуты.")
    private int duration;
}
