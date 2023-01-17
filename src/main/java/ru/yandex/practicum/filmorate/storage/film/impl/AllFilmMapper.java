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
            Integer idUser = 0;
            String idsUsers = "";
            String ids = "";
            try{
                ids = rowSet.getString("GENRE_ID");
                idsUsers = rowSet.getString("USER_ID");
            }catch (NullPointerException e){
                e.getMessage();
            }
            Film film = new Film(
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    rowSet.getDate("releaseDate").toLocalDate(),
                    rowSet.getInt("duration"),
                    new MPA(rowSet.getInt("MPARATING_RATING"), rowSet.getString("rating_name")));
            film.setId(rowSet.getInt("film_id"));
            film.setGenres(getGenresForFilm(rowSet, ids));
            film.setWhoLikedUserIds(getLikesForFilm(rowSet, idsUsers));

            return film;
        }

        private SortedSet<Genre> getGenresForFilm(ResultSet rowSet, String ids) throws SQLException {
                SortedSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
                if (!Objects.isNull(ids) && !ids.isEmpty()) {
                    int[] genreIds = Arrays.stream(ids.split(", ")).mapToInt(Integer::parseInt).toArray();
                    String[] genreNames = rowSet.getString("GENRE_NAME").split(", ");

                    for (int i = 0; i < genreIds.length; i++) {
                        genres.add(new Genre(genreIds[i], genreNames[i]));
                    }
            }
            return genres;
        }
    private Set<Integer> getLikesForFilm(ResultSet rowSet, String idsUsers) throws SQLException {
        Set<Integer> likes = new HashSet<>();
        if (!Objects.isNull(idsUsers) && !idsUsers.isEmpty()) {
            int[] usersIds = Arrays.stream(idsUsers.split(", ")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < usersIds.length; i++) {
                likes.add(usersIds[i]);
            }
        }
        return likes;
    }

    /*@Override
    public Film mapRow(ResultSet rowSet, int rowNum) throws SQLException {
        Integer idUser = 0;
        String idsUsers = "";
        String ids = "";
        try{
            ids = rowSet.getString("GENRE_ID");
            idsUsers = rowSet.getString("USER_ID");
        }catch (NullPointerException e){
            e.getMessage();
        }
        if (!Objects.isNull(idsUsers)){
            idUser = Integer.valueOf(idsUsers);
        }
        Film film = new Film(
                rowSet.getString("name"),
                rowSet.getString("description"),
                rowSet.getDate("releaseDate").toLocalDate(),
                rowSet.getInt("duration"),
                new MPA(rowSet.getInt("MPARATING_RATING"), rowSet.getString("rating_name")));
        film.setId(rowSet.getInt("film_id"));
        film.setGenres(getGenresForFilm(rowSet, ids));
        if (idUser != 0){
            film.getWhoLikedUserIds().add(idUser);
        }

        return film;
    }

    private SortedSet<Genre> getGenresForFilm(ResultSet rowSet, String ids) throws SQLException {
        SortedSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        if (!Objects.isNull(ids) && !ids.isEmpty()) {
            int[] genreIds = Arrays.stream(ids.split(", ")).mapToInt(Integer::parseInt).toArray();
            String[] genreNames = rowSet.getString("GENRE_NAME").split(", ");

            for (int i = 0; i < genreIds.length; i++) {
                genres.add(new Genre(genreIds[i], genreNames[i]));
            }
        }
        return genres;
    }*/
}
