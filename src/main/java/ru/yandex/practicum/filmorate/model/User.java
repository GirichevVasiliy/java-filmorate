package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private final Set<Integer> friendIds = new HashSet<>();
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    public void addFriendId(Integer idFriends) {
        this.friendIds.add(idFriends);
    }
}
