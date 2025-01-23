MERGE INTO genres AS target
USING (VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик')
) AS source (genre_id, genre_name)
ON target.genre_id = source.genre_id
WHEN MATCHED THEN
    UPDATE SET genre_name = source.genre_name
WHEN NOT MATCHED THEN
    INSERT (genre_id, genre_name) VALUES (source.genre_id, source.genre_name);

MERGE INTO rating AS target
USING (VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17')
) AS source (rating_id, rating_name)
ON target.rating_id = source.rating_id
WHEN MATCHED THEN
    UPDATE SET rating_name = source.rating_name
WHEN NOT MATCHED THEN
    INSERT (rating_id, rating_name) VALUES (source.rating_id, source.rating_name);

INSERT INTO users (login, email, name, birthday)
VALUES ('ukimi', 'tul@mail.ru', 'Tim Worker', '1990-12-05');

INSERT INTO films (name, description, release_date, duration, rating_id)
VALUES ('kdfjl', 'a;kdjfkahskgla;lkshdgs', '1967-12-09', 123, 3);