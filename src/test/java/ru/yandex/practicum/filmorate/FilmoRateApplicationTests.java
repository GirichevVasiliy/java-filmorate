
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.impl.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
    private final JdbcTemplate jdbcTemplate;
    User user0;
    Film film0;

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
        User userForUpdate = userStorage.updateUser(new User(id, "11111@ya.com", "non", "non",
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
    @DisplayName("Список всех пользователей")
    public void getAllUserTest() {
        Collection<User> allUsersForTest = jdbcTemplate.query("SELECT * FROM MODEL_USER;", (rs, rowNum) -> makeUser(rs));
        Collection<User> allUsers = userStorage.getAllUser();
        assertThat(allUsers)
                .isNotNull()
                .isEqualTo(allUsersForTest);
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

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
    private void initFilms(){
        film0 = new Film(1, "filmUpdate", "non", LocalDate.of(2023, 01, 01),
                500, new MPA(5, "NC-17"));
    }
    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Получение всего списка фильмов")
    public void getAllFilmsTest(){
        Collection<Film> allFilmsForTest = jdbcTemplate.query("SELECT * FROM MODEL_FILM AS mf INNER JOIN " +
                "MPA_RATING AS mpa ON mf.MPARATING_RATING = mpa.ID_MPA_RATING",  (rs, rowNum) -> makeFilm(rs));
        Collection<Film> allFilms = filmDbStorage.getAllFilms();
        assertThat(allFilmsForTest).isNotNull();
        assertThat(allFilms)
                .isNotNull()
                .isEqualTo(allFilmsForTest);
    }
    @Test
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление фильма")
    public void addFilmTest(){
        final int id = 1;
        initFilms();
        Film addFilm = filmDbStorage.addFilm(film0);
        Film filmForTest = filmDbStorage.getFilmById(id);
        Optional<Film> addFilmOptional = Optional.ofNullable(addFilm);
        assertThat(addFilm).isNotNull();
        assertThat(filmForTest).isNotNull();
        assertThat(addFilmOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", filmForTest.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", filmForTest.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("description", filmForTest.getDescription()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("releaseDate", filmForTest.getReleaseDate()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("duration", filmForTest.getDuration()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("mpa", filmForTest.getMpa()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("genres", filmForTest.getGenres()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("whoLikedUserIds", filmForTest.getWhoLikedUserIds()));
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Обновление фильма")
    public void updateFilmTest(){
        final int id = 1;
        Film filmForUpdate = filmDbStorage.updateFilm(new Film(id, "NEWfilmNEW", "NEW",
                LocalDate.of(2023, 01, 13), 999, new MPA( 3, "PG-13")));
        Film filmForTest = filmDbStorage.getFilmById(filmForUpdate.getId());
        Optional<Film> addFilmOptional = Optional.ofNullable(filmForUpdate);
        assertThat(filmForUpdate).isNotNull();
        assertThat(filmForTest).isNotNull();
        assertThat(addFilmOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", filmForTest.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", filmForTest.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("description", filmForTest.getDescription()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("releaseDate", filmForTest.getReleaseDate()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("duration", filmForTest.getDuration()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("mpa", filmForTest.getMpa()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("genres", filmForTest.getGenres()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("whoLikedUserIds", filmForTest.getWhoLikedUserIds()));
    }












    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new MPA(rs.getInt("id_mpa_rating"), rs.getString("rating_name")))
                .build();
    }
}