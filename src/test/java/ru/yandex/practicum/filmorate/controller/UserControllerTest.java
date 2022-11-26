package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    @DisplayName("Стандартный тест создания и валидации пользователя")
    void createUserAndValidationStandardTest() {
        final User user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.createUser(user1);
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user1), "Пользователь не сохранен"),
                () -> assertEquals(user1, userController.getUsers().get(user1.getId()),
                        "Пользователи не одинаковые")
        );
        final User user2 = User.builder()
                .email("ivanov@yandex.ru")
                .login("VladIvanov")
                .name("Vlad Ivanov")
                .birthday(LocalDate.parse("1989-01-01"))
                .build();
        userController.createUser(user2);
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user2), "Пользователь не сохранен"),
                () -> assertEquals(user2, userController.getUsers().get(user2.getId()),
                        "Пользователи не одинаковые")
        );
    }

    @Test
    @DisplayName("Tест создания пользователя и валидации пользователя с пустым адресом электронной почты")
    void createUserAndalidationIsEmptyEmailTest() {
        User user1 = User.builder()
                .email("")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Адрес электронной почты не может быть пустым.",
                    "Тест с пустым адресом электронной почты провален");
        }
    }

    @Test
    @DisplayName("Tест создания пользователя и валидации пользователя с неверным адресом электронной почты")
    void createUserAndValidationDoesNotContainSymbolDogTest() {
        User user1 = User.builder()
                .email("girichevyandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не прошла проверку, ошибка при вводе данных.",
                    "Тест с неверным адресом электронной почты провален");
        }
    }
    @Test
    @DisplayName("Tест создания пользователя и валидации пользователя с пробелом в логине")
    void createUserAndValidationSpaceInUserLoginTest() {
        User user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("Vasiliy Gir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Логин не может содержать пробелы.",
                    "Тест с неверным логином провален");
        }
    }

    @Test
    @DisplayName("Тест создания пользователя и валидации пользователя с отсутствующим именем пользователя")
    void createUserAndValidationSetNameTest() {
        final int idUser1 = 1;
        User user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.createUser(user1);
        User userControl = User.builder()
                .id(idUser1)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("VasiliyGir")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user1), "Пользователь не сохранен"),
                () -> assertEquals(userControl, userController.getUsers().get(user1.getId()),
                        "Пользователи не одинаковые")
        );
    }

    @Test
    @DisplayName("Тест создания пользователя и валидации пользователя из будущего")
    void createUserAndValidationWrongDateOfBirthTest() {
        User user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("2038-02-26"))
                .build();
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем.", "Тест с датой рождения провален");
        }

    }


    @Test
    void updateUser() {
    }

    @Test
    void findAllUsers() {
    }

}