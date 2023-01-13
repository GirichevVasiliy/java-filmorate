
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.impl.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final FriendDbStorage friendDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikesDbStorage likesDbStorage;
    private final MpaDbStorage mpaDbStorage;
    User user0;

    private void initUser() {
        user0 = User.builder()
                .email("Vigol@yandex.ru")
                .login("ViGol")
                .name("Viktoria Golovina")
                .birthday(LocalDate.parse("1991-01-01"))
                .build();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление пользователя")
    public void addUserTest() {
        initUser();
        User addUser = userStorage.addUser(user0);
        User userForTest = userStorage.getUserById(addUser.getId());
        Optional<User> addUserOptional = Optional.ofNullable(addUser);
        assertThat(addUser).isNotNull();
        assertThat(userForTest).isNotNull();
        assertThat(addUserOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userForTest.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userForTest.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userForTest.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userForTest.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userForTest.getBirthday()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("friendIds", userForTest.getFriendIds()));
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Обновление пользователя")
    public void updateUserTest() {
        final int id = 1;
        User userForUpdate = userStorage.updateUser(new User(1, "11111@ya.com", "non", "non",
                LocalDate.of(2022, 03, 22)));
        User userForTest = userStorage.getUserById(userForUpdate.getId());
        Optional<User> addUserOptional = Optional.ofNullable(userForUpdate);
        assertThat(userForUpdate).isNotNull();
        assertThat(userForTest).isNotNull();
        assertThat(addUserOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userForTest.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userForTest.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userForTest.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userForTest.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userForTest.getBirthday()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("friendIds", userForTest.getFriendIds()));
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Поиск пользователя по id")
    public void testFindUserById() {
        final int id = 1;
        User userForTest = userStorage.getUserById(id);
        Optional<User> userOptional = Optional.ofNullable(userForTest);
        assertThat(userForTest).isNotNull();
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userForTest.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userForTest.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userForTest.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userForTest.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userForTest.getBirthday()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("friendIds", userForTest.getFriendIds()));
    }


}