package ru.yandex.practicum.filmorate.model;

import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.time.LocalDate;

@SpringBootTest
public class UserTests {

        public Validator validator;

        @BeforeEach
        void buildValidator() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

    @Test
    void userWithoutViolationsTest() {
        User user = User.builder()
                .email("br@ya.ru")
                .login("suki")
                .name("Yana")
                .birthday(LocalDate.of(2001, 12, 5))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void userWithViolationsTest() {
        User userWithViolations = User.builder()
                .email("@ya.ru")
                .login("")
                .name("")
                .birthday(LocalDate.of(2221, 11, 25))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(userWithViolations);

        Assertions.assertEquals(3, violations.size());

        for (ConstraintViolation<User> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            switch (propertyPath) {
                case "email":
                    Assertions.assertEquals("email введен некорректно.", message);
                    break;
                case "login":
                    Assertions.assertEquals("Поле не должно быть пустым.", message);
                    break;
                case "birthday":
                    Assertions.assertEquals("Дата рождения не может быть в будущем.", message);
                    break;
            }
        }
    }

}
