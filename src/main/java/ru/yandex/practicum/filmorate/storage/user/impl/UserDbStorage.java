package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.mapper.UserMapperForFriends;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        int id = 0;
        try {
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String query = "INSERT INTO MODEL_USER (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?);";
            try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getEmail());
                pstmt.setString(2, user.getLogin());
                pstmt.setString(3, user.getName());
                pstmt.setDate(4, Date.valueOf(user.getBirthday()));
                pstmt.executeUpdate();
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int amountLines = jdbcTemplate.update("UPDATE MODEL_USER SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE USER_ID=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (amountLines == 0) {
            throw new ResourceNotFoundException("Запрашиваемый пользователь для обновления данных не найден");
        }
        try {
            User updateUser = jdbcTemplate.queryForObject("SELECT * FROM MODEL_USER WHERE user_id=?",
                    (rs, rowNum) -> makeUser(rs), user.getId());
            return updateUser;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Измененый пользователь не найден");
        }
    }

    @Override
    public Collection<User> getAllUser() {
        return jdbcTemplate.query("SELECT mu.USER_ID AS U_ID, mu.EMAIL, mu.LOGIN, mu.NAME, mu.BIRTHDAY, " +
                "TRIM(BOTH ']' from TRIM(BOTH '[' FROM ARRAY_AGG(uf.FRIEND_ID))) AS F_ID, " +
                "TRIM(BOTH ']' from TRIM(BOTH '[' FROM TRIM(BOTH '}' from TRIM(BOTH '{' FROM ARRAY_AGG(uf.STATUS))))) AS ST_F " +
                "FROM MODEL_USER AS mu " +
                "LEFT OUTER JOIN USERS_FRIENDS AS uf ON mu.USER_ID = uf.USER_ID " +
                "GROUP BY U_ID, mu.EMAIL, mu.LOGIN, mu.NAME, mu.BIRTHDAY;", new UserMapperForFriends());
    }

    @Override
    public User getUserById(int id) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM MODEL_USER WHERE USER_ID=?",
                    (rs, rowNum) -> makeUser(rs), id);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
        }
    }

    @Override
    public Collection<User> findAllFriendsToUser(Integer id) {
        String sql = "SELECT uf.FRIEND_ID AS U_ID, mu.EMAIL, mu.LOGIN, mu.NAME, mu.BIRTHDAY, " +
                "TRIM(BOTH ']' from TRIM(BOTH '[' FROM ARRAY_AGG(u.FRIEND_ID))) AS F_ID, " +
                "TRIM(BOTH '[' FROM TRIM(BOTH '}' from TRIM(BOTH '{' FROM ARRAY_AGG(u.STATUS)))) AS ST_F " +
                "FROM USERS_FRIENDS AS uf " +
                "LEFT JOIN MODEL_USER AS mu ON uf.FRIEND_ID = mu.USER_ID " +
                "LEFT JOIN USERS_FRIENDS AS u ON u.USER_ID = uf.FRIEND_ID " +
                "WHERE uf.USER_ID = ? AND uf.STATUS = true " +
                "GROUP BY U_ID, mu.EMAIL, mu.LOGIN, mu.NAME, mu.BIRTHDAY;";
        return jdbcTemplate.query(sql, new UserMapperForFriends(), id);
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
}
