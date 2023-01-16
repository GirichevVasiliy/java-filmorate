package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AllFilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rowSet, int rowNum) throws SQLException {

            String ids = rowSet.getString("GENRE_ID");
           String idsUsers = rowSet.getString("USER_ID");

            Film film = new Film(
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    rowSet.getDate("releaseDate").toLocalDate(),
                    rowSet.getInt("duration"),
                    new MPA(rowSet.getInt("MPARATING_RATING"), rowSet.getString("rating_name")));
            film.setId(rowSet.getInt("film_id"));
            film.setGenres(getGenresForFilm(rowSet, ids));
            film.setWhoLikedUserIds(setWhoLikedUserIds(rowSet, idsUsers));
            return film;
        }

        private SortedSet<Genre> getGenresForFilm(ResultSet rowSet, String ids) throws SQLException {
            SortedSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            if (!ids.contains("null")) {
                int[] genreIds = Arrays.stream(ids.split(", ")).mapToInt(Integer::parseInt).toArray();
                String[] genreNames = rowSet.getString("GENRE_NAME").split(", ");

                for (int i = 0; i < genreIds.length; i++) {
                    genres.add(new Genre(genreIds[i], genreNames[i]));
                }
            }
            return genres;
        }
    private List<Integer> setWhoLikedUserIds(ResultSet rowSet, String idsUsers) throws SQLException {
        List<Integer> like = new ArrayList<>();
        if (!like.contains("null")) {
            int[] userIds = Arrays.stream(idsUsers.split(", ")).mapToInt(Integer::parseInt).toArray();

            for (int i = 0; i < userIds.length; i++) {
                like.add(userIds[i]);
            }
        }
        return like;
    }
}
