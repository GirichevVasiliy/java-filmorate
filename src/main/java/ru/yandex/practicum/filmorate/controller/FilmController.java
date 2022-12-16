package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //**************** POST************************//
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        filmService.createFilm(film);
        return film;
    }

    //**************** PUT************************//
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        filmService.updateFilm(film);
        return film;
    }

    // пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeFilm(id, userId);
    }

    //**************** GET************************//
    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }
    @GetMapping("/{id}")
    public Film findFilmsForId(@PathVariable int id) {
        return filmService.getFilmById(id);
    }
    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public Collection<Film> findTopTenMostLikesFilms(@RequestParam Integer count) {
        return filmService.findTopTenMostLikesFilms(count);
    }
    //**************** DELETE ************************//

    // пользователь удаляет лайк.
    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeFilm (@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLikeFilm(id, userId);
    }





/*


            GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    Если значение параметра count не задано, верните первые 10.*/

}
