package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Collection<Integer> whoLikedUserIds = new HashSet<>();
    private Collection<Genre> genres = new ArrayList<>();
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
    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
