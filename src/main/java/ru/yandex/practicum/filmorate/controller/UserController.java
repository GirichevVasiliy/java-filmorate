package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        if (!users.containsValue(newUser)) {
            users.put(newUser.getEmail(), newUser);
            return newUser;
        } else {

        }
        return null;
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {

    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    public boolean userValidation(User user) {
        boolean isValidation = false;
        if (user != null) {
            if (user.getEmail().isBlank() || user.getEmail() == null) {
                throw new ValidationException("Адрес электронной почты не может быть пустым.");
            } else if (!user.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта не прошла проверку, ошибка при вводе данных.");
            }
            if (user.getLogin().isBlank()){
                throw new ValidationException("Логин не может быть пустым.");
            }else if (user.getLogin().contains(" ")){
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())){
                throw new ValidationException("Дата рождения не может быть в будущем.");
            } else {
                isValidation = true;
            }
        }
        return isValidation;
    }
}
