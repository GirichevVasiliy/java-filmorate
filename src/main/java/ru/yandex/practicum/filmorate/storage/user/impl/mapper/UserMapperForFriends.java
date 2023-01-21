package ru.yandex.practicum.filmorate.storage.user.impl.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserMapperForFriends implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String ids = rs.getString("F_ID");
        String status = rs.getString("ST_F");

        User user = new User(
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
        user.setId(rs.getInt("U_ID"));
        user.setFriendIds(getFriends(rs, ids, status));
        return user;
    }

    private Set<Integer> getFriends(ResultSet rs, String ids, String status) {
        Set<Integer> friendsIds = new HashSet<>();
        if (!ids.contains("null") && !status.contains("null") && !ids.isEmpty()) {
            String[] st = status.split(", ");
            int[] usersIds = Arrays.stream(ids.split(", ")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < usersIds.length; i++) {
                if (st[i].equals("TRUE")) {
                    friendsIds.add(usersIds[i]);
                }
            }
        }
        return friendsIds;
    }
}
