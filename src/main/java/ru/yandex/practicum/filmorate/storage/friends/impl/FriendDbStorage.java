package ru.yandex.practicum.filmorate.storage.friends.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorServer;
import ru.yandex.practicum.filmorate.storage.friends.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        try {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS_FRIENDS WHERE USER_ID=? AND FRIEND_ID = ?;",
                    userId, friendId);
            if (friendRows.next()) {
                jdbcTemplate.update("UPDATE USERS_FRIENDS SET STATUS = true WHERE ID_USERS_FRIENDS = ?;",
                        friendRows.getInt("id_users_friends"));
            } else {
                jdbcTemplate.update("INSERT INTO USERS_FRIENDS(USER_ID, FRIEND_ID, STATUS) VALUES ( ?,?,? );",
                        userId, friendId, true);
                jdbcTemplate.update("INSERT INTO USERS_FRIENDS(USER_ID, FRIEND_ID, STATUS) VALUES ( ?,?,? );",
                        friendId, userId, false);
            }
        } catch (RuntimeException e) {
            throw new ErrorServer("пользователь не существует");
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("UPDATE USERS_FRIENDS SET STATUS = false WHERE USER_ID=? AND FRIEND_ID=?;", userId, friendId);
    }

    @Override
    public List<Integer> getAllFriendByUser(int userId) {
        try {
            return jdbcTemplate.query("SELECT FRIEND_ID FROM USERS_FRIENDS WHERE USER_ID=?;", new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt("friend_id");
                }
            }, userId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
