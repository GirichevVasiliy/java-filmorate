package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User newUser) {
        if (!Objects.isNull(newUser)) {
            if (userVerification(newUser) && userValidation(newUser)) {
                log.info("Получен запрос на добавление нового пользователя " + newUser.getEmail());
                return userStorage.addUser(newUser);
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
            if (userStorage.getUsers().containsKey(user.getId())) {
                if (userValidation(user)) {
                    userForStorage = userStorage.updateUser(user);
                    log.info("Получен запрос на обновление пользователя " + user.getEmail());
                }
            } else {
                throw new ResourceNotFoundException("Пользователь не обновлен");
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
        if (userStorage.getUsers().containsKey(id) && id >= 0) {
            log.info("Получен запрос на получение информации о пользователе с ID = " + id);
            return userStorage.getUserById(id);
        } else {
            log.warn("Пользователь c ID: " + id + " не найден");
            throw new ResourceNotFoundException("Пользователь c ID: " + id + " не найден");
        }
    }

    public void addFriendToUser(Integer id, Integer friendId) {
        if (!Objects.equals(id, friendId)) {
            if (id >= 0 && userStorage.getUsers().containsKey(id)) {
                if (friendId >= 0 && userStorage.getUsers().containsKey(friendId)) {
                    userStorage.getUsers().get(id).setFriend(friendId);
                    userStorage.getUsers().get(friendId).setFriend(id);
                    log.info("Получен запрос на добавление в друзья пользователю с ID = " + id
                            + " от пользователя с ID = " + friendId);
                } else {
                    log.warn("Пользователь c ID: " + friendId + " не найден");
                    throw new ResourceNotFoundException("Пользователь c ID: " + friendId + " не найден");
                }
            } else {
                log.warn("Пользователь c ID: " + id + " не найден");
                throw new ResourceNotFoundException("Пользователь c ID: " + id + " не найден");
            }
        } else {
            throw new ValidationException("Пользователь не может добавить сам себя в друзья");
        }
    }

    public Collection<User> findAllFriendsToUser(@PathVariable Integer id) {
        final Collection<User> allFriends = new ArrayList<>();
        if (id >= 0 && userStorage.getUsers().containsKey(id)) {
            final User user = userStorage.getUsers().get(id);
            for (Integer idFriend : user.getFriends()) {
                allFriends.add(userStorage.getUsers().get(idFriend));
            }
            log.info("Получен запрос на получение списка всех друзей пользователя с ID = " + id);
        } else {
            throw new ResourceNotFoundException("Пользователь c ID: " + id + " не найден");
        }
        return allFriends;
    }

    public Collection<User> findListOfCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        final Collection<User> listOfCommonFriends = new ArrayList<>();
        if (!Objects.equals(id, otherId)) {
            if (id >= 0 && userStorage.getUsers().containsKey(id)) {
                if (otherId >= 0 && userStorage.getUsers().containsKey(otherId)) {
                    final User user1 = userStorage.getUsers().get(id);
                    final User user2 = userStorage.getUsers().get(otherId);
                    for (Integer idFriend : user1.getFriends()) {
                        if (user2.getFriends().contains(idFriend)) {
                            listOfCommonFriends.add(userStorage.getUsers().get(idFriend));
                        }
                    }
                    log.info("Получен запрос на получение списка общих друзей пользователя с ID = " + id
                            + " c пользователем с ID = " + otherId);
                } else {
                    log.warn("Пользователь c ID: " + otherId + " не найден");
                    throw new ResourceNotFoundException("Пользователь c ID: " + otherId + " не найден");
                }
            } else {
                log.warn("Пользователь c ID: " + id + " не найден");
                throw new ResourceNotFoundException("Пользователь c ID: " + id + " не найден");
            }
        } else {
            throw new ValidationException("Пользователь не может быть сам себе другом");
        }
        return listOfCommonFriends;
    }

    public void deleteFriendToUser(Integer id, Integer friendId) {
        if (!Objects.equals(id, friendId)) {
            if (id >= 0 && userStorage.getUsers().containsKey(id)) {
                if (friendId >= 0 && userStorage.getUsers().containsKey(friendId)) {
                    userStorage.getUsers().get(id).getFriends().remove(friendId);
                    log.info("Получен запрос на удаление друга с ID = " + friendId + "у пользователя с ID = " + id);
                } else {
                    throw new ResourceNotFoundException("Пользователь c ID: " + friendId + " не найден");
                }
            } else {
                throw new ResourceNotFoundException("Пользователь c ID: " + id + " не найден");
            }
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
        boolean isUserVerification = true;
        if (!userStorage.getUsers().isEmpty() && !userStorage.getEmails().isEmpty()) {
            if (userStorage.getEmails().contains(user.getEmail())) {
                log.warn("Пользователь с Email:" + user.getEmail() + " зарегистрирован ранее");
                isUserVerification = false;
            }
        }
        return isUserVerification;
    }
}
