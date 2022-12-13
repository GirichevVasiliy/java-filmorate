package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //**************** POST************************//
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    //**************** PUT************************//
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriendToUser(id, friendId);
    }

    //**************** GET************************//
    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserForId(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriendsToUser(@PathVariable Integer id) {
        return userService.findAllFriendsToUser(id);
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findListOfCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findListOfCommonFriends(id, otherId);
    }

    //**************** DELETE ************************//
    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendToUser(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriendToUser(id, friendId);
    }
}
