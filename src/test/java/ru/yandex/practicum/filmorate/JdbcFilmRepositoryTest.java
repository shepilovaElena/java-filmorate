package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.FilmDbRepository;
import ru.yandex.practicum.filmorate.repository.film.FilmRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbRepository.class, FilmRowMapper.class})
public class JdbcFilmRepositoryTest {

    @Autowired
    private final FilmDbRepository filmDbRepository;

    Film testFilm = Film.builder().id(1).name("kdfjl").description("a;kdjfkahskgla;lkshdgs")
            .releaseDate(LocalDate.of(1967,12,9)).duration(123).build();

    Film test2Film = Film.builder().id(1).name("kdfjl").description("a;kdjfkahskgla;lkshdgs")
            .releaseDate(LocalDate.of(1967,12,9)).duration(123).build();

    Film test3Film = Film.builder().id(2).name("Innocent").description(";ksdfh;kaskdhjf;k")
            .releaseDate(LocalDate.of(1989,1,9)).duration(183).build();



    @Test
    public void postFilmTest() {
        test3Film.setMpa(Mpa.builder().id(3).name("PG-13").build());

        Optional<Film> userOptional = Optional.ofNullable(filmDbRepository.postFilm(Film.builder().name("Innocent")
                .description(";ksdfh;kaskdhjf;k").releaseDate(LocalDate.of(1989, 1,9))
                .duration(183).mpa(Mpa.builder().id(3).name("PG-13").build()).build()));


        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(test3Film));

    }

    @Test
    public void putFilmTest() {
        test2Film.setMpa(Mpa.builder().id(2).name("PG").build());

        Optional<Film> userOptional = Optional.ofNullable(filmDbRepository.putFilm(Film.builder().id(1).name("kdfjl")
                .description("a;kdjfkahskgla;lkshdgs").releaseDate(LocalDate.of(1967, 12,9))
                .duration(123).mpa(Mpa.builder().id(2).name("PG").build()).build()));


        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(test2Film));

    }

    @Test
    public void getFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbRepository.getFilmById(1));
        testFilm.setMpa(Mpa.builder().id(3).name("PG-13").build());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

}
