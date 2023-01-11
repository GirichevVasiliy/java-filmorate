package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Set<Integer> friendIds;
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    public void addFriendId(Integer idFriends) {
        createFriends();
        this.friendIds.add(idFriends);
    }
    public void setFriends(List<Integer> friends) {
        createFriends();
        this.friendIds.addAll(friends);
    }
    private void createFriends() {
        if (friendIds == null) {
            friendIds = new HashSet<>();
        }
    }
}
