package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Collection<Integer> whoLikedUserIds;
    private Collection<Genre> genres;
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

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        createGenres();
    }
    public Film(String name, String description, LocalDate releaseDate, int duration, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        createGenres();
    }

    public void addLike(Integer userId) {
        createLikes();
        this.whoLikedUserIds.add(userId);
    }

    public void deleteLike(Integer userId) {
        createLikes();
        whoLikedUserIds.remove(userId);
    }

    public int getLikeCount() {
        createLikes();
        return whoLikedUserIds.size();
    }

    public Collection<Integer> getWhoLikedUserIds() {
        createLikes();
        return whoLikedUserIds;
    }

    public void addGenre(Genre genre) {
        createGenres();
        this.genres.add(genre);
    }

    public Collection<Genre> getGenres() {
        createGenres();
        return genres;
    }

    private void createLikes() {
        if (whoLikedUserIds == null) {
            whoLikedUserIds = new HashSet<>();
        }
    }

    private void createGenres() {
        if (genres == null) {
            genres = new ArrayList<>();
        }
    }
}
