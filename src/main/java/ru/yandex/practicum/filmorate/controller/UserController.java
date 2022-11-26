package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
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
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        if (!users.containsValue(newUser) && userValidation(newUser) && newUser != null) {
            generateIdUser(newUser);
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            throw new ValidationException("Пользователь не задан");
        }
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {

    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private boolean userValidation(User user) {
        boolean isValidation = false;
        if (user != null) {
            if (user.getEmail().isBlank()) {
                throw new ValidationException("Адрес электронной почты не может быть пустым.");
            } else if (!user.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта не прошла проверку, ошибка при вводе данных.");
            }
            if (user.getLogin().isBlank()) {
                throw new ValidationException("Логин не может быть пустым.");
            } else if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем.");
            } else {
                isValidation = true;
            }
        }
        return isValidation;
    }
    private void generateIdUser(User user){
          if (user.getId() == 0) {
            user.setId(++id);
        } else if ((user.getId() > id) || (user.getId() < id)){
            user.setId(++id);
        }
    }


}
