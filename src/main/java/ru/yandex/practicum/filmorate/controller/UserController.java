package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    @Getter
    private final Map<Integer, User> users = new LinkedHashMap<>();
    private int id;

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        if (userVerification(newUser) && userValidation(newUser)) {
            generateIdUser(newUser);
            users.put(newUser.getId(), newUser);
            log.info("Получен запрос к эндпоинту: Создания пользователя - выполнено успешно");
            return newUser;
        } else {
            log.warn("Получен запрос к эндпоинту: Создания пользователя - не выполнен");
            throw new ValidationException("Пользователь не сохранен, такой Email уже зарегистрирован");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (userValidation(user)) {
                if (!(users.get(user.getId()).getEmail().equals(user.getEmail()))) {
                    final String email = user.getEmail();
                    users.get(user.getId()).setEmail(email);
                }
                if (!(users.get(user.getId()).getLogin().equals(user.getLogin()))) {
                    final String login = user.getLogin();
                    users.get(user.getId()).setLogin(login);
                }
                if (Objects.isNull(user.getName())) {
                    user.setName(user.getLogin());
                }
                if (!(users.get(user.getId()).getName().equals(user.getName()))) {
                    final String name = user.getName();
                    users.get(user.getId()).setName(name);
                }
                if (!(users.get(user.getId()).getBirthday().equals(user.getBirthday()))) {
                    final LocalDate birthday = user.getBirthday();
                    users.get(user.getId()).setBirthday(birthday);
                }
            }
            return user;
        } else {
            throw new ValidationException("Пользователь не создан");
        }
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
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

    private void generateIdUser(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        } else if ((user.getId() > id) || (user.getId() < id)) {
            user.setId(++id);
        }
    }

    private boolean userVerification(User user) {
        boolean isUserVerification = false;
        if (!users.isEmpty()) {
            for (User userSearch : users.values()) {
                if (userSearch.getEmail().equals(user.getEmail())) {
                    log.warn("Пользователь с Email:" + user.getEmail() + " зарегистрирована ранее");
                    break;
                } else {
                    log.info("Почта не была зарегистрирована ранее");
                    isUserVerification = true;
                }
            }

        } else {
            isUserVerification = true;
        }
        return isUserVerification;
    }


}
