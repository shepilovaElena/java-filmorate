package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
public class User {
    private long id = 0;
    @Email(message = "email введен некорректно.")
    @NotBlank(message = "Поле не должно быть пустым.")
    @NotNull(message = "Поле не должно быть пустым.")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    @NotNull(message = "Поле не должно быть пустым.")
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();
}
