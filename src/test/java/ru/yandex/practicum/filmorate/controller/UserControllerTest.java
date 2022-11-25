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
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void findAllUsers() {
    }

    @Test
    @DisplayName("Стандартный тест валидации пользователя")
    void userValidationStandardTest() {
        User user = User.builder()
                .id(0)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        assertTrue(userController.userValidation(user), "Валидация юзера не прошла");
    }

     @Test
     @DisplayName("Tест валидации пользователя с пустым адресом электронной почты")
     void userValidationIsEmptyEmailTest() {
         User user = User.builder()
                 .id(0)
                 .email("")
                 .login("VasiliyGir")
                 .name("Vasiliy Girichev")
                 .birthday(LocalDate.parse("1987-02-26"))
                 .build();
         try {
             userController.userValidation(user);
         } catch (ValidationException e){
             assertEquals(e.getMessage(), "Адрес электронной почты не может быть пустым.",
                     "Тест с пустым адресом электронной почты провален");
         }

     }
     @Test
     @DisplayName("Tест валидации пользователя с неверным адресом электронной почты")
     void userValidationDoesNotContainSymbolDogTest() {
         User user = User.builder()
                 .id(0)
                 .email("girichevyandex.ru")
                 .login("VasiliyGir")
                 .name("Vasiliy Girichev")
                 .birthday(LocalDate.parse("1987-02-26"))
                 .build();
         try {
             userController.userValidation(user);
         } catch (ValidationException e){
             assertEquals(e.getMessage(), "Электронная почта не прошла проверку, ошибка при вводе данных.",
                     "Тест с неверным адресом электронной почты провален");
         }
     }
    @Test
    @DisplayName("Tест валидации пользователя с отсутствующим именем пользователя")
    void userValidationSetNameTest() {
        User user = User.builder()
                .id(0)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        userController.userValidation(user);
        User userControl = User.builder()
                .id(0)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("VasiliyGir")
                .birthday(LocalDate.parse("1987-02-26"))
                .build();
        assertEquals(user, userControl, "объекты не одинаковые");
    }

    @Test
    @DisplayName("Tест валидации пользователя из будущего")
    void userValidationWrongDateOfBirthTest() {
        User user = User.builder()
                .id(0)
                .email("girichev@yandex.ru")
                .login("VasiliyGir")
                .name("Vasiliy Girichev")
                .birthday(LocalDate.parse("2038-02-26"))
                .build();
        try {
            userController.userValidation(user);
        } catch (ValidationException e){
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем.", "Тест с датой рождения провален");
        }

    }
}