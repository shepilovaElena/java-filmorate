package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @Email(message = "email введен некорректно.")
    @NotBlank(message = "Поле не должно быть пустым.")
    @NotNull(message = "Поле не должно быть пустым.")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    @NotNull(message = "Поле не должно быть пустым.")
    private Instant birthday;
}
