package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getAllFriendByUser(int userId);
}
