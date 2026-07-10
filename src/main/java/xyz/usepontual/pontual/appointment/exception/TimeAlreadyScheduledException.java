package xyz.usepontual.pontual.appointment.exception;

public class TimeAlreadyScheduledException extends RuntimeException {
    public TimeAlreadyScheduledException(String message) {
        super(message);
    }
}
