package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User user0;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @BeforeEach
    void init() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
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

    void initUsersForCommonFriend() {
        user3 = User.builder()
                .email("sidorov@yandex.ru")
                .login("VovaSidorov")
                .name("Sidorov Vova")
                .birthday(LocalDate.parse("1966-03-02"))
                .build();
        userController.createUser(user3);
        user4 = User.builder()
                .email("fofanov@yandex.ru")
                .login("IvanFofanov")
                .name("Ivan Fofanov")
                .birthday(LocalDate.parse("1933-09-01"))
                .build();
        userController.createUser(user4);
        user5 = User.builder()
                .email("Golovin@yandex.ru")
                .login("VladGolovin")
                .name("Vlad Golovin")
                .birthday(LocalDate.parse("1999-06-01"))
                .build();
        userController.createUser(user5);
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(user1, userController.findUserForId(user1.getId()),
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
                () -> assertTrue(userController.findAllUsers().contains(user2), "Пользователь не сохранен"),
                () -> assertEquals(user2, userController.findUserForId(user2.getId()),
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(user1, userController.findUserForId(user1.getId()),
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
        assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);}, "Тест создания и валидации несуществующего пользователя провален");
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
        assertThrows(ValidationException.class, () -> {
            userController.createUser(user1);}, "Tест создания пользователя и валидации пользователя " +
                "с пустым адресом электронной почты провален");
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
        assertThrows(ValidationException.class, () -> {
            userController.createUser(user1);}, "Tест создания пользователя и валидации пользователя " +
                "с неверным адресом электронной почты провален");
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
        assertThrows(ValidationException.class, () -> { userController.createUser(user1);},
                "Tест создания пользователя и валидации пользователя с пробелом в логине провален");
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(userControl, userController.findUserForId(user1.getId()),
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(userControl, userController.findUserForId(user1.getId()),
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
        assertThrows(ValidationException.class, () -> { userController.createUser(user1);},
                "Тест создания пользователя и валидации пользователя из будущего провален");
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
                () -> assertTrue(userController.findAllUsers().contains(user1Control), "" +
                        "Пользователь не сохранен"),
                () -> assertEquals(user1Control, userController.findUserForId(user1Control.getId()),
                        "Пользователи не одинаковые")
        );
        assertAll(
                () -> assertTrue(userController.findAllUsers().contains(user2Control), "" +
                        "Пользователь не сохранен"),
                () -> assertEquals(user2Control, userController.findUserForId(user2Control.getId()),
                        "Пользователи не одинаковые")
        );
        assertAll(
                () -> assertTrue(userController.findAllUsers().contains(user0), "" +
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
        assertThrows(ValidationException.class, () -> {userController.updateUser(user1);},
                "Tест обновления пользователя и валидации с пустым адресом электронной почты провален");
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
        assertThrows(ValidationException.class, () -> {
            userController.updateUser(user1);}, "Tест обновления пользователя и валидации пользователя " +
                "с пробелом в логине провален");
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(userControl, userController.findUserForId(user1.getId()),
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
                () -> assertTrue(userController.findAllUsers().contains(user1), "Пользователь не сохранен"),
                () -> assertEquals(userControl, userController.findUserForId(user1.getId()),
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
        assertThrows(ValidationException.class, () -> {
            userController.updateUser(user1);}, "Тест создания пользователя и валидации " +
                "пользователя из будущего провален");
    }

    @Test
    @DisplayName("Тест обновления и валидации несуществующего пользователя")
    void updateUserAndValidationUserIsNullTest() {
        User user = null;
        assertThrows(RuntimeException.class, () -> {userController.updateUser(user);}, "Тест обновления " +
                "и валидации несуществующего пользователя провален");
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

    @Test
    @DisplayName("Поиск пользователя по id")
    void getUserByIdTest() {
        initUsers();
        final int idUserForSearch = 1;
        User userSearch = userController.findUserForId(idUserForSearch);
        assertEquals(userSearch, user0, "Тест поиска пользователя по id провален");
    }

    @Test
    @DisplayName("Поиск пользователя по id")
    void getUserByBadIdTest() {
        initUsers();
        final int idUserForSearch = 99999;
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.findUserForId(idUserForSearch);
        }, "Тест поиска пользователя по id провален");
    }

    @Test
    @DisplayName("Добавление друзей пользователю")
    void addFriendToUserTest() {
        initUsers();
        userController.addFriendToUser(user0.getId(), user1.getId());
        assertAll(
                () -> assertTrue(userController.findUserForId(user0.getId()).getFriendIds().contains(user1.getId()),
                        "Пользователь id = " + user1.getId() + " не найден у пользователя " + user0.getId()),
                () -> assertTrue(userController.findUserForId(user1.getId()).getFriendIds().contains(user0.getId()),
                        "Пользователь id = " + user0.getId() + " не найден у пользователя " + user1.getId())
        );
    }

    @Test
    @DisplayName("Добавление друзей пользователю с одинаковым id")
    void addFriendToUserEqualsIdTest() {
        initUsers();
        assertThrows(ValidationException.class, () -> {
            userController.addFriendToUser(user0.getId(), user0.getId());
        }, "Тест добавления друзей пользователю с одинаковым id провален");
    }

    @Test
    @DisplayName("Добавление друзей пользователю с неверным id")
    void addFriendToUserBadIdTest() {
        initUsers();
        int idBad1 = -1;
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.addFriendToUser(user0.getId(), idBad1);
        }, "Тест создания и валидации повторяющегося пользователя провален");
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.addFriendToUser(idBad1, user0.getId());
        }, "Тест добавление друзей пользователю с неверным id провален");
    }

    @Test
    @DisplayName("Получение всего списка друзей пользователя")
    void findAllFriendsToUserTest() {
        initUsers();
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        Collection<User> idFriendListUser0 = userController.findAllFriendsToUser(user0.getId());
        assertAll(
                () -> assertTrue(idFriendListUser0.contains(user1),
                        "Пользователь id = " + user1.getId() + " не найден в списке друзей у пользователя "
                                + user0.getId()),
                () -> assertTrue(idFriendListUser0.contains(user2),
                        "Пользователь id = " + user2.getId() + " не найден в списке друзей у пользователя "
                                + user0.getId())
        );
        userController.addFriendToUser(user1.getId(), user0.getId());
        userController.addFriendToUser(user1.getId(), user2.getId());
        Collection<User> idFriendListUser1 = userController.findAllFriendsToUser(user1.getId());
        assertAll(
                () -> assertTrue(idFriendListUser1.contains(user0),
                        "Пользователь id = " + user1.getId() + " не найден в списке друзей у пользователя "
                                + user0.getId()),
                () -> assertTrue(idFriendListUser1.contains(user2),
                        "Пользователь id = " + user2.getId() + " не найден в списке друзей у пользователя "
                                + user0.getId())
        );
    }

    @Test
    @DisplayName("Получение всего списка друзей пользователя c неверным id")
    void findAllFriendsToUserBadIdFriendTest() {
        initUsers();
        int idBad1 = -1;
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.findAllFriendsToUser(idBad1);
        }, "Получение всего списка друзей пользователя c неверным id провален");
    }

    @Test
    @DisplayName("Получение всего списка одинаковых друзей пользователей")
    void findListOfCommonFriendsTest() {
    initUsers();
    initUsersForCommonFriend();
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        userController.addFriendToUser(user0.getId(), user3.getId());
        userController.addFriendToUser(user1.getId(), user3.getId());
        Collection<User> commonUser = userController.findListOfCommonFriends(user0.getId(), user1.getId());
        assertAll(
                () -> assertTrue(commonUser.contains(user3),
                        "У пользователя id = " + user0.getId() + " не найден общий друг " + user3.getId()
                                + "с пользователем " + user1.getId())
        );
    }

    @Test
    @DisplayName("Получение всего списка одинаковых друзей пользователей")
    void findListOfCommonFriendsBadIdTest() {
        int idBad1 = -1;
        initUsers();
        initUsersForCommonFriend();
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        userController.addFriendToUser(user0.getId(), user3.getId());
        userController.addFriendToUser(user1.getId(), user3.getId());
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.findListOfCommonFriends(user0.getId(),idBad1);
        }, "Получение всего списка общих друзей пользователя c неверным id провален");
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.findListOfCommonFriends(idBad1, user0.getId());
        }, "Получение всего списка общих друзей пользователя c неверным id провален");
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.findListOfCommonFriends(idBad1, user0.getId());
        }, "Получение всего списка общих друзей пользователя c неверным id провален");
        assertThrows(ValidationException.class, () -> {
            userController.findListOfCommonFriends(idBad1, idBad1);
        }, "Получение всего списка общих друзей пользователя c неверным id провален");
    }
    @Test
    @DisplayName("Удаление друга пользователя по ID")
    void deleteFriendToUserForIdTest(){
        initUsers();
        initUsersForCommonFriend();
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        userController.addFriendToUser(user0.getId(), user3.getId());
        userController.addFriendToUser(user1.getId(), user3.getId());
        userController.deleteFriendToUser(user0.getId(), user1.getId());
        Collection<User> idFriendListUser0 = userController.findAllFriendsToUser(user0.getId());
        assertAll(
                () -> assertFalse(idFriendListUser0.contains(user1),
                        "У пользователя id = " + user0.getId() + " найден друг " + user1.getId())
        );
        userController.deleteFriendToUser(user0.getId(), user2.getId());
        Collection<User> idFriendListUser00 = userController.findAllFriendsToUser(user0.getId());
        assertAll(
                () -> assertFalse(idFriendListUser00.contains(user2),
                        "У пользователя id = " + user0.getId() + " найден друг " + user2.getId())
        );
    }
    @Test
    @DisplayName("Удаление друга пользователя по неверному ID")
    void deleteFriendToUserForBadIdTest(){
        int idBad1 = -1;
        initUsers();
        initUsersForCommonFriend();
        userController.addFriendToUser(user0.getId(), user1.getId());
        userController.addFriendToUser(user0.getId(), user2.getId());
        userController.addFriendToUser(user0.getId(), user3.getId());
        userController.addFriendToUser(user1.getId(), user3.getId());
        userController.deleteFriendToUser(user0.getId(), user1.getId());
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.deleteFriendToUser(user0.getId(),idBad1);
        }, "Тест удаления друга пользователя по неверному ID провален");
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.deleteFriendToUser(idBad1, user0.getId());
        }, "Тест удаления друга пользователя по неверному ID провален");
              assertThrows(ValidationException.class, () -> {
            userController.deleteFriendToUser(idBad1, idBad1);
        }, "Тест удаления друга пользователя по неверному ID провален");
    }
}