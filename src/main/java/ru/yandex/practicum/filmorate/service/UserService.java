package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
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
              return userStorage.addUser(newUser);
            } else {
                log.warn("Получен запрос к эндпоинту: Создания пользователя - не выполнен");
                throw new ValidationException("Пользователь " + newUser.getEmail() +
                        " не сохранен, такой Email уже зарегистрирован");
            }
        } else {
            throw new RuntimeException("Ошибка, пользователь не задан");
        }
    }

    public User updateUser(User user) {
        User userForStorage = null;
        if (!Objects.isNull(user)) {
            if (userStorage.getUsers().containsKey(user.getId())) {
                if (userValidation(user)) {
                    userForStorage = userStorage.updateUser(user);
                }
            } else {
                throw new ResourceNotFoundException("Пользователь не обновлен");
            }
        } else {
            throw new RuntimeException("Ошибка, пользователь не задан");
        }
        return userForStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.getAllUser();
    }

    public User getUserById(int id){
        if (userStorage.getUsers().containsKey(id)){
            return userStorage.getUserById(id);
        } else {
            throw new ResourceNotFoundException("Фильм c ID: " + id + " не найден");
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
