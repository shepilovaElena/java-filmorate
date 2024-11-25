package ru.yandex.practicum.filmorate.model;

import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.time.LocalDate;

@SpringBootTest
public class FilmTests {

    public Validator validator;

    @BeforeEach
    void buildValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void filmWithoutViolationsTest() {
        Film film = Film.builder()
                .name("uri")
                .description("описание")
                .releaseDate(LocalDate.of(1905, 12, 9))
                .duration(75)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void filmWithViolationsTest() {
        Film filmWithViolations = Film.builder()
                .name("")
                .description("")
                .releaseDate(LocalDate.of(1905, 12, 9))
                .duration(0)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithViolations);

        Assertions.assertEquals(3, violations.size());

        for (ConstraintViolation<Film> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            switch (propertyPath) {
                case "name":
                    Assertions.assertEquals("Поле не должно быть пустым.", message);
                    break;
                case  "description":
                    Assertions.assertEquals("Описание не должно быть пустым.", message);
                    break;
                case "duration":
                    Assertions.assertEquals("Длительность фильма не может быть меньше минуты.", message);
                    break;
            }
        }
    }

    @Test
    void filmWithLongDuration() {
        Film longDurationFilm = Film.builder()
                .name("Копье отваги.")
                .description("Во времена правления сёгунов Токугава был на окраине Эдо небольшой торговый район Фукагава. " +
                        "Именно там и начинается действие фильма. Ронин Фува Мондо занимается тем, " +
                        "что преподаёт искусство копья в своём маленьком додзё, да посылает слугу собирать утиные яйца на продажу." +
                        " А поскольку жалости к ученикам он не знает, то именно утки и дают ему средства к существованию. " +
                        "Однако больно уж хрупко благосостояние, основанное на таком товаре. И это становится особенно ясно после того, " +
                        "как дочь хатамото Судзуэ, гнавшая от обиды своего коня, сбивает с ног слугу Фувы, торговавшего непрочным товаром.")
                .releaseDate(LocalDate.of(1961, 6, 1))
                .duration(84)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(longDurationFilm);

        String messageAboutLongDescription = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("description"))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(null);

        Assertions.assertEquals("Описание не должно превышать 200 символов.", messageAboutLongDescription);
    }

    @Test
    void filmWithMinDuration() {
        Film film = Film.builder()
                .name("uri")
                .description("описание")
                .releaseDate(LocalDate.of(1905, 12, 9))
                .duration(1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.isEmpty());
    }
}
