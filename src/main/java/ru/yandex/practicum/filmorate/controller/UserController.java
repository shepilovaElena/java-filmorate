package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Получен запрос на получение всех пользователей.");
        return userService.getAllUsers();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя.");
        checkUserLogin(user);
        return userService.postUser(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на изменение пользователя.");
        checkUserLogin(user);
        return userService.putUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable long id, @PathVariable long friendId) {
        if (!checkId(id)) {
            throw new NotFoundException("Пользователь с id" + id + " не найден.");
        }
        if (!checkId(friendId)) {
            throw new NotFoundException("Пользователь с id" + friendId + " не найден.");
        }
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void  deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        if (!checkId(id)) {
            throw new NotFoundException("Пользователь с id" + id + " не найден.");
        }
        if (!checkId(friendId)) {
            throw new NotFoundException("Пользователь с id" + friendId + " не найден.");
        }
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriendsList(@PathVariable long id) {
        if (!checkId(id)) {
            throw new NotFoundException("Пользователь с id" + id + " не найден.");
        }
        return userService.getUserFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getJointFriends(@PathVariable long id, @PathVariable long otherId) {
        if (!checkId(id)) {
            throw new NotFoundException("Пользователь с id" + id + " не найден.");
        }
        if (!checkId(otherId)) {
            throw new NotFoundException("Пользователь с id" + otherId + " не найден.");
        }
        return userService.getJointFriends(id, otherId);
    }

    private void checkUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя {} содержит пробел.", user.getLogin());
            throw new ValidationException("Логин не может содержат знак пробела.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    private boolean checkId(long id) {
        boolean check = true;
        Optional<Long> userId = userService.getAllUsers().stream()
                .map(user -> user.getId())
                .filter(i -> i == id)
                .findFirst();
        if (userId.isEmpty()) {
            check = false;
        }
        return check;
    }
}
