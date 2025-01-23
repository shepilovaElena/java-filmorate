package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.rating.MpaRepository;

import java.util.List;

@Service
public class MpaService {
    private final MpaRepository mpaDbRepository;
    private final NamedParameterJdbcOperations jdbcOperations;

    public MpaService(MpaRepository mpaDbRepository, NamedParameterJdbcOperations jdbcOperations) {
        this.mpaDbRepository = mpaDbRepository;
        this.jdbcOperations = jdbcOperations;
    }

    public List<Mpa> getAllRatings() {
        return mpaDbRepository.getAllRatings();
    }

    public Mpa getRatingById(int id) {
        checkId(id);
        return mpaDbRepository.getRatingById(id);
    }

    private void checkId(int id) {
        String checkMpaQuery = "SELECT EXISTS (SELECT 1 FROM rating WHERE rating_id = :id)";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        if (jdbcOperations.queryForObject(checkMpaQuery, param, Integer.class) == 0) {
            throw new NotFoundException("Рейтинга с id " + id + " не существует.");
        }
    }
}
