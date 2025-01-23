package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.rating.MpaDbRepository;
import ru.yandex.practicum.filmorate.repository.rating.MpaRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbRepository.class, MpaRowMapper.class})
public class JdbcMpaRepositoryTest {

    @Autowired
    private final MpaDbRepository mpaDbRepository;

    Mpa testMpa = Mpa.builder().id(2).name("PG").build();
    List<Mpa> testMpaList = List.of(Mpa.builder().id(1).name("G").build(), testMpa, Mpa.builder().id(3).name("PG-13").build(),
            Mpa.builder().id(4).name("R").build(), Mpa.builder().id(5).name("NC-17").build());

    @Test
    public void getRatingById() {
        Optional<Mpa> userOptional = Optional.ofNullable(mpaDbRepository.getRatingById(2));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 2)
                )
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(testMpa));
    }

    @Test
    public void getAllRatings() {

        Optional<List<Mpa>> userOptional = Optional.ofNullable(mpaDbRepository.getAllRatings());

        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(testMpaList));

    }
}
