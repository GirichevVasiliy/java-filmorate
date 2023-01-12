package ru.yandex.practicum.filmorate.storage.friends;

import java.util.List;

public interface FriendStorage {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<Integer> getAllFriendByUser(int userId);
}
