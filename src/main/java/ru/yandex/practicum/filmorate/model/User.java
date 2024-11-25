package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    @Builder.Default
    private int id = 0;
    @Email(message = "email введен некорректно.")
    @NotBlank(message = "Поле не должно быть пустым.")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    @NotNull(message = "Поле не должно быть пустым.")
    private LocalDate birthday;

}
