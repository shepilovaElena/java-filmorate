package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    @Builder.Default
    private int id = 0;
    @NotBlank(message = "Поле не должно быть пустым.")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым.")
    @Size(message = "Описание не должно превышать 200 символов.", max = 200)
    private String description;
    @NotNull(message = "Поле не должно быть пустым.")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Длительность фильма не может быть меньше минуты.")
    private int duration;
}
