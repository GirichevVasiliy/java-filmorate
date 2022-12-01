package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int id;

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        if (!Objects.isNull(newUser)) {
            if (userVerification(newUser) && userValidation(newUser)) {
                generateIdUser(newUser);
                users.put(newUser.getId(), newUser);
                emails.add(newUser.getEmail());
                log.info("Получен запрос к эндпоинту: Создания пользователя - выполнено успешно");
                return newUser;
            } else {
                log.warn("Получен запрос к эндпоинту: Создания пользователя - не выполнен");
                throw new ValidationException("Пользователь " + newUser.getEmail() +
                        " не сохранен, такой Email уже зарегистрирован");
            }
        } else {
            throw new RuntimeException("Ошибка, пользователь не задан");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!Objects.isNull(user)) {
            if (users.containsKey(user.getId())) {
                if (userValidation(user)) {
                    final User savedUser = users.get(user.getId());
                    if (!(savedUser.getEmail().equals(user.getEmail()))) {
                        final String email = user.getEmail();
                        savedUser.setEmail(email);
                    }
                    if (!(savedUser.getLogin().equals(user.getLogin()))) {
                        final String login = user.getLogin();
                        savedUser.setLogin(login);
                    }
                    if (Objects.isNull(user.getName())) {
                        user.setName(user.getLogin());
                    }
                    if (!(savedUser.getName().equals(user.getName()))) {
                        final String name = user.getName();
                        savedUser.setName(name);
                    }
                    if (!(savedUser.getBirthday().equals(user.getBirthday()))) {
                        final LocalDate birthday = user.getBirthday();
                        savedUser.setBirthday(birthday);
                    }
                }
                return user;
            } else {
                throw new ResourceNotFoundException("Пользователь не обновлен");
            }
        } else {
            throw new RuntimeException("Ошибка, пользователь не задан");
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
        }
    }

    private boolean userVerification(User user) {
        boolean isUserVerification = true;
        if (!users.isEmpty() && !emails.isEmpty()) {
            if (emails.contains(user.getEmail())) {
                log.warn("Пользователь с Email:" + user.getEmail() + " зарегистрирован ранее");
                isUserVerification = false;
            }
        }
        return isUserVerification;
    }
}
