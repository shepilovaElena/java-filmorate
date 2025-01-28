package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreDbRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbRepository.class, GenreRowMapper.class})
public class JdbcGenreRepositoryTest {

    @Autowired
    private final GenreDbRepository genreDbRepository;

    Genre genreTest = Genre.builder().id(3).name("Мультфильм").build();
    List<Genre> genresTestList = List.of(Genre.builder().id(1).name("Комедия").build(),
            Genre.builder().id(2).name("Драма").build(), genreTest,
            Genre.builder().id(4).name("Триллер").build(),
            Genre.builder().id(5).name("Документальный").build(),
            Genre.builder().id(6).name("Боевик").build());

    @Test
    public void getGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreDbRepository.getGenreById(3));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 3)
                )
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(genreTest));
    }

    @Test
    public void getAllGenres() {
        Optional<List<Genre>> userOptional = Optional.ofNullable(genreDbRepository.getAllGenres());

        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(genresTestList));
    }
}
