package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;

@RestController
@RequestMapping("/mpa")

public class MpaController {
    private final MpaService mpaService;
@Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }
}
