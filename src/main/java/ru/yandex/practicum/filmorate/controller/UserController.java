package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {
    Map<String, User> users = new HashMap<>();

    @PostMapping
    public void createUser(@RequestBody User user){

    }

    @PutMapping
    public void updateUser(@RequestBody User user){

    }

    @GetMapping
    public Collection<User> findAllUsers(){
        return users.values();
    }
}
