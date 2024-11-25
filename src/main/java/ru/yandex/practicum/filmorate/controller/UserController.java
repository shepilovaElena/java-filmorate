package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int counter;

    private int getNextId() {
        if (counter == 0) {
            counter = 1;
        } else {
            counter = counter + 1;
        }
        return counter;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Получен запрос на получение всех пользователей.");
        return users.values();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя.");
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя {} содержит пробел.", user.getLogin());
            throw new ValidationException("Логин не может содержат знак пробела.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        int nextId = getNextId();
        user.setId(nextId);
        users.put(nextId,user);
        log.info("Пользователь успешно добавлен.");
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на изменение пользователя.");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно изменен.");
            return user;
        } else {
            log.warn("Пользователь с id {} не существует", user.getId());
            throw new ValidationException("Пользователь с таким id не найден");
        }
    }
}
