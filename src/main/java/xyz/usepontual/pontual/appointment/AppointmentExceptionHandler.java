package xyz.usepontual.pontual.appointment;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.usepontual.pontual.appointment.exception.AppointmentNotFoundException;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@RestControllerAdvice
public class AppointmentExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentExceptionHandler.class);

    @ExceptionHandler(TimeAlreadyScheduledException.class)
    ProblemDetail handleTimeAlreadyScheduledException(TimeAlreadyScheduledException e, HttpServletRequest request) {
        logger.warn("Time slot overlaps with an existing appointment for this provider {}", request.getRequestURI(), e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    ProblemDetail handleFindAppointmentByIdResponse(AppointmentNotFoundException e, HttpServletRequest request) {
        logger.warn("Appointment {} not found", request.getRequestURI(), e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
