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
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> likes = new HashSet<>();
    public void setLikes(Integer like) {
        this.likes.add(like);
    }
    public Set<Integer> getLikes() {
        return this.likes;
    }
    public int getCountLikes(){
        return likes.size();
    }
}
