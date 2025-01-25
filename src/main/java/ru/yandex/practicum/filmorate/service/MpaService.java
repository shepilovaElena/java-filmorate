package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.rating.MpaRepository;

import java.util.List;

@Service
public class MpaService {
    private final MpaRepository mpaDbRepository;

    public MpaService(MpaRepository mpaDbRepository, NamedParameterJdbcOperations jdbcOperations) {
        this.mpaDbRepository = mpaDbRepository;
    }

    public List<Mpa> getAllRatings() {
        return mpaDbRepository.getAllRatings();
    }

    public Mpa getRatingById(int id) {
        return mpaDbRepository.getRatingById(id);
    }


}
