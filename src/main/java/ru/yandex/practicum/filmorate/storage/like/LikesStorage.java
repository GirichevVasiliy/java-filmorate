package ru.yandex.practicum.filmorate.storage.like;

import java.util.Collection;
import java.util.List;

public interface LikesStorage {
    void addLike(int userId, int filmId);
    void deleteLike(int userId, int filmId);
    Collection<Integer> getFilmLikeId(int filmId);
}
