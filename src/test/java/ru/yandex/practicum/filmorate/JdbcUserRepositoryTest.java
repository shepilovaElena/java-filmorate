package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserDbRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbRepository.class, UserRowMapper.class})
public class JdbcUserRepositoryTest {

    @Autowired
    private final UserDbRepository userDbRepository;

    User testUser = User.builder().id(1).login("ukimi").email("tul@mail.ru").name("Tim Worker")
            .birthday(LocalDate.of(1990,12,5)).build();
    User postTestUser = User.builder().id(2).login("urt").email("jim@mail.com").name("Anton")
            .birthday(LocalDate.of(1987, 4,7)).build();
    User putTestUser = User.builder().id(1).login("urt").email("jim@mail.com").name("Marina")
            .birthday(LocalDate.of(1987, 4,7)).build();


    @Test
    public void testGetUserById() {

        Optional<User> userOptional = Optional.ofNullable(userDbRepository.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                )
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(testUser));
    }

    @Test
    public void  testPostUser() {

        Optional<User> userOptional = Optional.ofNullable(userDbRepository.postUser(User.builder().login("urt")
                        .email("jim@mail.com").name("Anton").birthday(LocalDate.of(1987, 4,7)).build()));

        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(postTestUser));
    }

    @Test
    public void testPutUser() {
        Optional<User> userOptional = Optional.ofNullable(userDbRepository.putUser(User.builder().id(1).login("urt")
                        .email("jim@mail.com").name("Marina").birthday(LocalDate.of(1987, 4,7)).build()));

        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(putTestUser));
    }
}
