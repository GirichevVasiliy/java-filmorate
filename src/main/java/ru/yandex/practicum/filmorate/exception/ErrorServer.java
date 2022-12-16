package ru.yandex.practicum.filmorate.exception;

public class ErrorServer extends RuntimeException{
    public ErrorServer(String message) {
        super(message);
    }
}
