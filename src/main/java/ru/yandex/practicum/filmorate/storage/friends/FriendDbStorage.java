package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorServer;

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
    public List<Integer> getAllFriendByUser(int userId) {
        try {
            return jdbcTemplate.query("SELECT friend_id FROM USERS_FRIENDS WHERE user_id=?", new RowMapper<Integer>() {
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
