package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUser();

    User getUserById(int id);
    public Map<Integer, User> getUsers();
    public Set<String> getEmails();

}
