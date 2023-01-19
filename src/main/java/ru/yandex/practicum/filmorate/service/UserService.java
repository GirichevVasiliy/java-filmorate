package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User createUser(User newUser) {
        if (!Objects.isNull(newUser)) {
            if (userVerification(newUser) && userValidation(newUser)) {
                log.info("Получен запрос на добавление нового пользователя " + newUser.getEmail());
                userStorage.addUser(newUser);
                return newUser;
            } else {
                log.warn("Получен запрос к эндпоинту: Создания пользователя - не выполнен");
                throw new ValidationException("Пользователь " + newUser.getEmail() +
                        " не сохранен, такой Email уже зарегистрирован");
            }
        } else {
            throw new ResourceNotFoundException("Ошибка, пользователь не задан");
        }
    }

    public User updateUser(User user) {
        User userForStorage = null;
        if (!Objects.isNull(user)) {
            if (userValidation(user)) {
                userForStorage = userStorage.updateUser(user);
                user.setFriends(friendStorage.getAllFriendByUser(userForStorage.getId()));
                log.info("Получен запрос на обновление пользователя " + user.getEmail());
            }
        } else {
            throw new UserException("Ошибка, пользователь не задан");
        }
        return userForStorage;
    }

    public Collection<User> findAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userStorage.getAllUser();
    }

    public User getUserById(Integer id) {
        log.info("Получен запрос на получение информации о пользователе с ID = " + id);
        User user = userStorage.getUserById(id);
        user.setFriends(friendStorage.getAllFriendByUser(user.getId()));
        return user;
    }

    public void addFriendToUser(Integer id, Integer friendId) {
        log.info("Получен запрос на добавление в друзья пользователю с ID = " + id
                + " от пользователя с ID = " + friendId);
        try {
            friendStorage.addFriend(id, friendId);
        } catch (ErrorServer e) {
            throw new ResourceNotFoundException("Пользователь не добавлен в друзья");
        }
    }

    public Collection<User> findAllFriendsToUser(Integer id) {
       /* final Collection<User> allFriends = new ArrayList<>();

        User userForSearch = userStorage.getUserById(id);
        userForSearch.setFriends(friendStorage.getAllFriendByUser(id));

        List<User> us = new ArrayList<>(userStorage.getAllUser());
        if (!Objects.isNull(userForSearch.getFriendIds())) {
            us.stream().forEach(user -> {
                if (userForSearch.getFriendIds().contains(user.getId())) {
                    allFriends.add(user);
                }
            });
        }*/
        log.info("Получен запрос на получение списка всех друзей пользователя с ID = " + id);
        return userStorage.findAllFriendsToUser(id);
    }

    public Collection<User> findListOfCommonFriends(Integer id, Integer otherId) {
        final Collection<User> listOfCommonFriends = new ArrayList<>();
        if (!Objects.equals(id, otherId)) {
            User user1 = userStorage.getUserById(id);
            user1.setFriends(friendStorage.getAllFriendByUser(user1.getId()));
            User user2 = userStorage.getUserById(otherId);
            user2.setFriends(friendStorage.getAllFriendByUser(user2.getId()));
            for (Integer idFriend : user1.getFriendIds()) {
                if (user2.getFriendIds().contains(idFriend)) {
                    listOfCommonFriends.add(userStorage.getUserById(idFriend));
                }
            }
            log.info("Получен запрос на получение списка общих друзей пользователя с ID = " + id);

        } else {
            throw new ValidationException("Пользователь не может быть сам себе другом");
        }
        return listOfCommonFriends;
    }

    public void deleteFriendToUser(Integer id, Integer friendId) {
        if (!Objects.equals(id, friendId)) {
            log.info("Получен запрос на удаление пользователя");
            friendStorage.deleteFriend(id, friendId);
        } else {
            throw new ValidationException("Пользователь не может удалить себя из своего списка друзей");
        }
    }

    private boolean userValidation(User user) {
        boolean isValidation = false;
        if (user != null) {
            if (user.getEmail().isBlank()) {
                log.warn("Валидация электронной почты завершена ошибкой");
                throw new ValidationException("Адрес электронной почты не может быть пустым.");
            } else if (!user.getEmail().contains("@")) {
                log.warn("Валидация электронной почты завершена ошибкой");
                throw new ValidationException("Электронная почта не прошла проверку, ошибка при вводе данных.");
            }
            if (user.getLogin().isBlank()) {
                log.warn("Валидация логина пользователя завершена ошибкой");
                throw new ValidationException("Логин не может быть пустым.");
            } else if (user.getLogin().contains(" ")) {
                log.warn("Валидация логина пользователя завершена ошибкой");
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (Objects.isNull(user.getName())) {
                user.setName(user.getLogin());
            }
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Валидация даты рождения пользователя завершена ошибкой");
                throw new ValidationException("Дата рождения не может быть в будущем.");
            } else {
                log.info("Валидация пользователя выполнена успешно");
                isValidation = true;
            }
        }
        return isValidation;
    }

    private boolean userVerification(User user) {
        Collection<User> listAllUsers = findAllUsers();
        boolean isUserVerification = true;
        if (listAllUsers.contains(user)) {
            log.warn("Пользователь с Email:" + user.getEmail() + " зарегистрирован ранее");
            isUserVerification = false;
        }
        return isUserVerification;
    }
}
