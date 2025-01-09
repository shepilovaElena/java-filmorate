//package ru.yandex.practicum.filmorate.storage.user;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//@Data
//public class InMemoryUserStorage implements UserStorage {
//    private Map<Long, User> users = new HashMap<>();
//    private long counter = 0;
//
//    public Collection<User> getAllUsers() {
//        return users.values();
//    }
//
//    public User getUserById(long id) {
//        return users.get(id);
//    }
//
//    public User postUser(User user) {
//        long nextId = getNextId();
//        user.setId(nextId);
//        users.put(nextId,user);
//        log.info("Пользователь успешно добавлен.");
//        return user;
//    }
//
//    public User putUser(User user) {
//        if (users.containsKey(user.getId())) {
//            users.put(user.getId(), user);
//            log.info("Пользователь успешно изменен.");
//            return user;
//        } else {
//            log.warn("Пользователь с id {} не существует", user.getId());
//            throw new NotFoundException("Пользователь с таким id не найден");
//        }
//    }
//
//    private long getNextId() {
//        counter = counter + 1;
//        return counter;
//    }
//
//}
