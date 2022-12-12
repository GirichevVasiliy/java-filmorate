package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user0;
    private User user1;
    private User user2;

    @BeforeEach
    void init() {
       // userController = new UserController();
    }

    void initUsers() {
        user0 = User.builder()
                .email("petrov@yandex.ru")
                .login("Petr")
                .name("Stanislav Petrov")
                .birthday(LocalDate.parse("1961-05-21"))
                .build();
        userController.createUser(user0);
        user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.createUser(user1);
        user2 = User.builder()
                .email("ivanov@yandex.ru")
                .login("VladIvanov")
                .name("Vlad Ivanov")
                .birthday(LocalDate.parse("1989-01-01"))
                .build();
        userController.createUser(user2);
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
    @DisplayName("Тест создания и валидации повторяющегося пользователя")
    void createUserAndValidationRepeatingTest() {
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
        assertThrows(ValidationException.class, () -> {
            userController.createUser(user1);
        }, "Тест создания и валидации повторяющегося пользователя провален");
    }

    @Test
    @DisplayName("Тест создания и валидации несуществующего пользователя")
    void createUserAndValidationUserIsNullTest() {
        User user = null;
        try {
            userController.createUser(user);
        } catch (RuntimeException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Ошибка, пользователь не задан",
                            "Тест обновления несуществующего пользователя провален провален")
            );
        }
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
    @DisplayName("Тест создания пользователя и валидации пользователя с отсутствующим именем пользователя null")
    void createUserAndValidationNameIsNullTest() {
        final int idUser1 = 1;
        User user1 = User.builder()
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name(null)
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
    @DisplayName("Обновление пользователей")
    void updateUserStandardTest() {
        final int idUser0 = 1;
        final int idUser1 = 2;
        final int idUser2 = 3;
        initUsers();
        final User user1Control = User.builder()
                .id(idUser1)
                .email("girichev2022@yandex.ru")
                .login("VasiliyGir87")
                .name("Vasilii Girichev")
                .birthday(LocalDate.parse("1987-02-27"))
                .build();
        userController.updateUser(user1Control);
        final User user2Control = User.builder()
                .id(idUser2)
                .email("ivanov@yandex.ru")
                .login("VladIvanov")
                .name("Vlad Ivanov")
                .birthday(LocalDate.parse("1989-01-01"))
                .build();
        userController.updateUser(user2Control);
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user1Control), "" +
                        "Пользователь не сохранен"),
                () -> assertEquals(user1Control, userController.getUsers().get(user1Control.getId()),
                        "Пользователи не одинаковые")
        );
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user2Control), "" +
                        "Пользователь не сохранен"),
                () -> assertEquals(user2Control, userController.getUsers().get(user2Control.getId()),
                        "Пользователи не одинаковые")
        );
        assertAll(
                () -> assertTrue(userController.getUsers().containsValue(user0), "" +
                        "Пользователь не сохранен"),
                () -> assertEquals(idUser0, user0.getId(), "id не совпал"),
                () -> assertEquals(user0.getEmail(), "petrov@yandex.ru", "Почта пользователя не совпала"),
                () -> assertEquals(user0.getLogin(), "Petr", "Логин пользователя не совпал"),
                () -> assertEquals(user0.getName(), "Stanislav Petrov", "Имя пользователя не совпало"),
                () -> assertEquals(user0.getBirthday(), LocalDate.parse("1961-05-21"),
                        "Дата рождения пользователя не совпала")
        );
    }

    @Test
    @DisplayName("Tест обновления пользователя и валидации с пустым адресом электронной почты")
    void updateUserAndValidationIsEmptyEmailTest() {
        initUsers();
        final int idUser = 1;
        User user1 = User.builder()
                .id(idUser)
                .email("")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        try {
            userController.updateUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Адрес электронной почты не может быть пустым.",
                    "Тест с пустым адресом электронной почты провален");
        }
    }

    @Test
    @DisplayName("Tест обновления пользователя и валидации пользователя с пробелом в логине")
    void updateUserAndValidationSpaceInUserLoginTest() {
        initUsers();
        final int idUser = 1;
        User user1 = User.builder()
                .id(idUser)
                .email("girichev@yandex.ru")
                .login("Vasiliy Gir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        try {
            userController.updateUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Логин не может содержать пробелы.",
                    "Тест с неверным логином провален");
        }
    }

    @Test
    @DisplayName("Тест обновления пользователя и валидации пользователя с отсутствующим именем пользователя")
    void updateUserAndValidationSetNameTest() {
        final int idUser1 = 1;
        initUsers();
        User user1 = User.builder()
                .id(idUser1)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.updateUser(user1);
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
    @DisplayName("Тест создания пользователя и валидации пользователя с отсутствующим именем пользователя null")
    void updateUserAndValidationNameIsNullTest() {
        final int idUser1 = 1;
        initUsers();
        final User user1 = User.builder()
                .id(idUser1)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name(null)
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.updateUser(user1);
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
    void updateUserAndValidationWrongDateOfBirthTest() {
        final int idUser1 = 1;
        initUsers();
        final User user1 = User.builder()
                .id(idUser1)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("2038-02-26"))
                .build();
        try {
            userController.updateUser(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем.", "Тест с датой рождения провален");
        }
    }

    @Test
    @DisplayName("Тест обновления и валидации несуществующего пользователя")
    void updateUserAndValidationUserIsNullTest() {
        User user = null;
        try {
            userController.updateUser(user);
        } catch (RuntimeException e) {
            assertAll(
                    () -> assertEquals(e.getMessage(), "Ошибка, пользователь не задан",
                            "Тест обновления несуществующего пользователя провален провален")
            );
        }
    }

    @Test
    @DisplayName("Получение списка всех пользователей пользователей")
    void findAllUsers() {
        final int size = 3;
        initUsers();
        List<User> listOfAllUsers = new ArrayList<>(userController.findAllUsers());
        assertAll(
                () -> assertEquals(size, listOfAllUsers.size(), "Размер списка всех пользователей больше, " +
                        "тест провален"),
                () -> assertTrue(listOfAllUsers.contains(user0), "Пользователь id = 0 не найден"),
                () -> assertTrue(listOfAllUsers.contains(user1), "Пользователь id = 1 не найден"),
                () -> assertTrue(listOfAllUsers.contains(user2), "Пользователь id = 2 не найден")
        );
    }
}