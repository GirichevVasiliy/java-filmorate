package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Collection<Integer> friendIds;
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    public User(int id, @NonNull String email, @NonNull String login, String name, @NonNull LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

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
