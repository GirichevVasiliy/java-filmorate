package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private Duration duration;

}
