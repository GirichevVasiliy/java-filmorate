package ru.yandex.practicum.filmorate.storage.user.impl.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String ids = "";
        try {
            ids = rs.getString("FRIEND_ID");
        } catch (NullPointerException e) {
            e.getMessage();
        }
        User user = new User(
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
        user.setId(rs.getInt("USER_ID"));
        user.setFriendIds(getFriends(rs, ids));
        return user;
    }

    private Set<Integer> getFriends(ResultSet rs, String ids) {
        Set<Integer> friendsIds = new HashSet<>();
        if (!ids.contains("null") && !ids.isEmpty()) {
            int[] usersIds = Arrays.stream(ids.split(", ")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < usersIds.length; i++) {
                friendsIds.add(usersIds[i]);
            }
        }
        return friendsIds;
    }
}
