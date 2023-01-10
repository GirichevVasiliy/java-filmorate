package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        try {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from USERS_FRIENDS where user_id =? " +
                    "and friend_id = ?", userId, friendId);
            if (friendRows.next()) {
                jdbcTemplate.update("UPDATE USERS_FRIENDS SET STATUS = true where id_users_friends = ?",
                        friendRows.getInt("id_users_friends"));
            } else {
                jdbcTemplate.update("INSERT INTO USERS_FRIENDS(user_id, friend_id, status) values ( ?,?,? );",
                        userId, friendId, true);
                jdbcTemplate.update("INSERT INTO USERS_FRIENDS(user_id, friend_id, status) values ( ?,?,? );",
                        friendId, userId, false);
            }
        } catch (RuntimeException e1) {
            throw new ErrorServer("пользователь не существует");
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("UPDATE USERS_FRIENDS SET status = false WHERE user_id=? AND friend_id=?", userId, friendId);
    }

    @Override
    public List<User> getAllFriendByUser(int userId) {
        try {
            return jdbcTemplate.query("SELECT * FROM USERS_FRIENDS WHERE user_id=?",
                    new BeanPropertyRowMapper<>(User.class), userId);
        } catch (Exception e){
            return new ArrayList<>();
        }
    }
}
