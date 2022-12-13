package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();
    public void setFriend(Integer idFriends) {
        this.friends.add(idFriends);
    }

    public Set<Integer> getFriends() {
        return this.friends;
    }
}
