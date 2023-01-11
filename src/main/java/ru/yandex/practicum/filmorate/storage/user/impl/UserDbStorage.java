package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getLogin());
                pstmt.setString(3, user.getEmail());
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
        if (amountLines == 0){
            throw new ResourceNotFoundException("Запрашиваемый пользователь для обновления данных не найден");
        }
        try {
            User updateUser = jdbcTemplate.queryForObject("SELECT * FROM MODEL_USER WHERE user_id=?",
                    (rs, rowNum) -> makeUser(rs), user.getId());
            return updateUser;
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Измененый пользователь не найден");
        }
    }

    @Override
    public Collection<User> getAllUser() {
        return jdbcTemplate.query("SELECT * FROM MODEL_USER;", (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getUserById(int id) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM MODEL_USER WHERE USER_ID=?",
                    new BeanPropertyRowMapper<>(User.class), id);
            return user;
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
        }
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
