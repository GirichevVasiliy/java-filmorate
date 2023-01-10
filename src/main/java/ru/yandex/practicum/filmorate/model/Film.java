package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private final Set<Integer> whoLikedUserIds = new HashSet<>();
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private int duration;
    @NonNull
    private MPA mpa;

    public void addLike(Integer userId) {
        this.whoLikedUserIds.add(userId);
    }

    public int getLikeCount() {
        return whoLikedUserIds.size();
    }
}
