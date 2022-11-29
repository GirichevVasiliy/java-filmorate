package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
