package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Проверка создание объекта")
    void createUser() {
        final int idUser1 = 0;
        User user = User.builder()
                .email("ivanov@yandex.ru")
                .login("VladIvanov")
                .name("Vlad Ivanov")
                .birthday(LocalDate.parse("1989-01-01"))
                .build();
        assertAll(
                () -> assertEquals(idUser1, user.getId(), "id не совпал"),
                () -> assertEquals(user.getEmail(), "ivanov@yandex.ru", "Почта пользователя не совпала"),
                () -> assertEquals(user.getLogin(), "VladIvanov", "Логин пользователя не совпал"),
                () -> assertEquals(user.getName(), "Vlad Ivanov", "Имя пользователя не совпало"),
                () -> assertEquals(user.getBirthday(), LocalDate.parse("1989-01-01"),
                        "Дата рождения пользователя не совпала")

        );
    }

    @Test
    @DisplayName("Проверка создание объекта без почты")
    void createUserEmailNull() {
        assertThrows(NullPointerException.class, () -> {
            User.builder()
                    .email(null)
                    .login("VladIvanov")
                    .name("Vlad Ivanov")
                    .birthday(LocalDate.parse("1989-01-01"))
                    .build();
        }, "Тест по созданию объекта без без почты");
    }

    @Test
    @DisplayName("Проверка создание объекта без логина")
    void createUserLoginNull() {
        assertThrows(NullPointerException.class, () -> {
            User.builder()
                    .email("ivanov@yandex.ru")
                    .login(null)
                    .name("Vlad Ivanov")
                    .birthday(LocalDate.parse("1989-01-01"))
                    .build();
        }, "Тест по созданию объекта без без почты");
    }

    @Test
    @DisplayName("Проверка создание объекта без имени")
    void createUserNameNull() {
        User user = User.builder()
                .email("ivanov@yandex.ru")
                .login("VladIvanov")
                .name(null)
                .birthday(LocalDate.parse("1989-01-01"))
                .build();
        assertNull(user.getName());

    }

    @Test
    @DisplayName("Проверка создание объекта без даты рождения")
    void createUserBirthdayNull() {
        assertThrows(NullPointerException.class, () -> {
            User.builder()
                    .email("ivanov@yandex.ru")
                    .login("VladIvanov")
                    .name("Vlad Ivanov")
                    .birthday(null)
                    .build();
        }, "Тест по созданию объекта без без почты");
    }
}

