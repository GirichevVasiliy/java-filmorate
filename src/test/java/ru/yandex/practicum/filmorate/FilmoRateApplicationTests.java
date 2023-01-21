
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.impl.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

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
        Collection<User> allUsers = userStorage.getAllUser();
        int sizeList = 4;
        assertThat(allUsers)
                .isNotNull();
        assertThat(allUsers.size() == sizeList);
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

    private void initFilms() {
        film0 = new Film(1, "filmUpdate", "non", LocalDate.of(2023, 01, 01),
                500, new MPA(5, "NC-17"));
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Получение всего списка фильмов")
    public void getAllFilmsTest() {
        Collection<Film> allFilms = filmDbStorage.getAllFilms();
        assertThat(allFilms)
                .isNotNull();
        assertThat((allFilms).size() == 7);
    }

    @Test
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление фильма")
    public void addFilmTest() {
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
    public void updateFilmTest() {
        final int id = 1;
        Film filmForUpdate = filmDbStorage.updateFilm(new Film(id, "NEWfilmNEW", "NEW",
                LocalDate.of(2023, 01, 13), 999, new MPA(3, "PG-13")));
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

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Поиск фильма по id")
    public void testFindFilmById() {
        final int id = 1;
        Film filmForTest = filmDbStorage.getFilmById(id);
        Optional<Film> addFilmOptional = Optional.ofNullable(filmForTest);
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

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление в друзья")
    public void addFriend() {
        final int id3 = 3;
        final int id4 = 4;
        friendDbStorage.addFriend(id4, id3);
        User user = getUserById(id4);
        assertThat(user.getFriendIds().contains(id3)).isTrue();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Удаление из друзей")
    public void deleteFriendTest() {
        final int idUser1 = 1;
        final int idUser2 = 2;
        friendDbStorage.deleteFriend(idUser1, idUser2);
        User user = getUserById(idUser1);
        assertThat(user.getFriendIds().contains(idUser2)).isFalse();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Список всех друзей")
    public void getAllFriendByUserTest() {
        final int id = 1;
        final int idFriend2 = 2;
        final int idFriend3 = 3;
        final int size = 2;
        Collection<Integer> allFriendByUser = friendDbStorage.getAllFriendByUser(id);
        assertThat(allFriendByUser.contains(idFriend2)).isTrue();
        assertThat(allFriendByUser.contains(idFriend3)).isTrue();
        assertThat(allFriendByUser.size() == size).isTrue();
    }

    @Test
    @DisplayName("Список всех жанров")
    public void getAllGenreTest() {
        final int size = 6;
        Collection<Genre> allGenre = new ArrayList<>(genreDbStorage.getAll());
        assertThat(allGenre.size() == size).isTrue();
        assertThat(allGenre.contains(new Genre(1, "Комедия"))).isTrue();
        assertThat(allGenre.contains(new Genre(2, "Драма"))).isTrue();
        assertThat(allGenre.contains(new Genre(3, "Мультфильм"))).isTrue();
        assertThat(allGenre.contains(new Genre(4, "Триллер"))).isTrue();
        assertThat(allGenre.contains(new Genre(5, "Документальный"))).isTrue();
        assertThat(allGenre.contains(new Genre(6, "Боевик"))).isTrue();
    }

    @Test
    @DisplayName("Поиск жанра по ID")
    public void getByIdGenreTest() {
        final int id = 1;
        Genre genreSearchForID = genreDbStorage.getById(id);
        Optional<Genre> genreOptional = Optional.ofNullable(genreSearchForID);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", genreSearchForID.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", genreSearchForID.getName()));
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Поиск жанра по ID")
    public void getByFilmIdGenreTest() {
        final int id = 1;
        final int size = 2;
        Collection<Genre> allGenreByFilmId = new ArrayList<>(genreDbStorage.getByFilmId(id));
        assertThat(allGenreByFilmId.size() == size).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(1, "Комедия"))).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(2, "Драма"))).isTrue();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление жанра")
    public void assignGenreTest() {
        final int idFilm = 1;
        final int idGere = 3;
        final int size = 3;
        genreDbStorage.assignGenre(idFilm, idGere);
        Collection<Genre> allGenreByFilmId = new ArrayList<>(genreDbStorage.getByFilmId(idFilm));
        assertThat(allGenreByFilmId.size() == size).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(1, "Комедия"))).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(2, "Драма"))).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(3, "Мультфильм"))).isTrue();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Удаление жанра")
    public void deleteGenreTest() {
        final int idFilm = 1;
        final int size = 0;
        genreDbStorage.delete(idFilm);
        Collection<Genre> allGenreByFilmId = new ArrayList<>(genreDbStorage.getByFilmId(idFilm));
        assertThat(allGenreByFilmId.size() == size).isTrue();
        assertThat(allGenreByFilmId.contains(new Genre(1, "Комедия"))).isFalse();
        assertThat(allGenreByFilmId.contains(new Genre(2, "Драма"))).isFalse();
    }

    @Test
    @Sql(value = {"classpath:data-test-likes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Добавление лайка")
    public void addLikeTest() {
        final int idFilm = 2;
        final int idUser1 = 1;
        final int idUser2 = 2;
        final int size = 2;
        likesDbStorage.addLike(idUser1, idFilm);
        likesDbStorage.addLike(idUser2, idFilm);
        Collection<Integer> allLike = new ArrayList<>(likesDbStorage.getFilmLikeId(idFilm));
        assertThat(allLike.size() == size).isTrue();
        assertThat(allLike.contains(idUser1)).isTrue();
        assertThat(allLike.contains(idUser2)).isTrue();
    }

    @Test
    @Sql(value = {"classpath:data-test-likes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Удаление лайка")
    public void deleteLikeTest() {
        final int idFilm = 2;
        final int idUser1 = 1;
        final int idUser2 = 2;
        final int size = 0;
        likesDbStorage.addLike(idUser1, idFilm);
        likesDbStorage.addLike(idUser2, idFilm);
        likesDbStorage.deleteLike(idUser1, idFilm);
        likesDbStorage.deleteLike(idUser2, idFilm);
        Collection<Integer> allLike = new ArrayList<>(likesDbStorage.getFilmLikeId(idFilm));
        assertThat(allLike.size() == size).isTrue();
        assertThat(allLike.contains(idUser1)).isFalse();
        assertThat(allLike.contains(idUser2)).isFalse();
    }

    @Test
    @Sql(value = {"classpath:data-test-obects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Поиск лайков у фильма")
    public void getFilmLikeId() {
        final int idFilm = 1;
        final int idUser1 = 1;
        final int idUser2 = 2;
        final int size = 2;
        Collection<Integer> allLike = new ArrayList<>(likesDbStorage.getFilmLikeId(idFilm));
        assertThat(allLike.size() == size).isTrue();
        assertThat(allLike.contains(idUser1)).isTrue();
        assertThat(allLike.contains(idUser2)).isTrue();
    }

    @Test
    @DisplayName("Список всех жанров")
    public void getAllMpaTest() {
        final int size = 5;
        Collection<MPA> mpa = new ArrayList<>(mpaDbStorage.getAll());
        assertThat(mpa.size() == size).isTrue();
        assertThat(mpa.contains(new MPA(1, "G"))).isTrue();
        assertThat(mpa.contains(new MPA(2, "PG"))).isTrue();
        assertThat(mpa.contains(new MPA(3, "PG-13"))).isTrue();
        assertThat(mpa.contains(new MPA(4, "R"))).isTrue();
        assertThat(mpa.contains(new MPA(5, "NC-17"))).isTrue();
    }

    @Test
    @DisplayName("Список всех жанров")
    public void getByIdMpaTest() {
        final int id = 1;
        MPA mpa = mpaDbStorage.getById(id);
        Optional<MPA> mpaOptional = Optional.ofNullable(mpa);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", mpa.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", mpa.getName()));
    }


    private User getUserById(Integer id) {
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from model_user where user_id = ?", id);
        if (userRows.next()) {
            user = User.builder()
                    .id(userRows.getInt("user_id"))
                    .name(userRows.getString("name"))
                    .login(userRows.getString("login"))
                    .email(userRows.getString("email"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .friendIds(friendDbStorage.getAllFriendByUser(id))
                    .build();
        }
        return user;
    }
}