CREATE TABLE IF NOT EXISTS  genres(
genre_id integer AUTO_INCREMENT PRIMARY KEY,
genre_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS  rating(
rating_id integer AUTO_INCREMENT PRIMARY KEY,
rating_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
user_id integer AUTO_INCREMENT PRIMARY KEY,
login varchar NOT NULL,
email varchar NOT NULL,
name varchar,
birthday timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS films(
film_id integer AUTO_INCREMENT PRIMARY KEY,
name varchar NOT NULL,
description varchar(200) NOT NULL,
release_date timestamp NOT NULL,
duration integer,
rating_id integer REFERENCES rating(rating_id)
);

CREATE TABLE IF NOT EXISTS likes(
like_id integer AUTO_INCREMENT PRIMARY KEY,
film_id integer REFERENCES films(film_id) NOT NULL,
user_id integer REFERENCES users(user_id) NOT NULL
);

CREATE TABLE IF NOT EXISTS  film_genres_list(
film_genre_id integer AUTO_INCREMENT PRIMARY KEY,
genre_id integer REFERENCES genres(genre_id) NOT NULL,
film_id integer REFERENCES films(film_id) NOT NULL
);


CREATE TABLE  IF NOT EXISTS friendship(
friendship_id INTEGER AUTO_INCREMENT PRIMARY KEY,
user_id integer REFERENCES users(user_id),
user_friend_id integer REFERENCES users(user_id)
);