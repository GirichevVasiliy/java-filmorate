package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int id;


    @Override
    public User addUser(User user) {
        if (!Objects.isNull(user)) {
            generateIdUser(user);
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            log.info("Получен запрос к эндпоинту: Создания пользователя - выполнено успешно");
        } else {
            throw new ErrorServer("Пользователь  не сохранен, ошибка сервера");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!Objects.isNull(user)) {
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
        } else {
            throw new ErrorServer("Пользователь  не обновлен, ошибка сервера");
        }
        return user;
    }

    @Override
    public Collection<User> getAllUser() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public Set<String> getEmails() {
        return emails;
    }

    private void generateIdUser(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        }
    }
}
