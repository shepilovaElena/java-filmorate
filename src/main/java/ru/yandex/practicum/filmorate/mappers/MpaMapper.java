package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    public static MpaDto toDto(Mpa rating) {
        return MpaDto.builder()
                .id(rating.getId())
                .build();
    }

    public static Mpa toModel(MpaDto ratingDto) {
        return Mpa.builder()
                .id(ratingDto.getId())
                .build();
    }
}
