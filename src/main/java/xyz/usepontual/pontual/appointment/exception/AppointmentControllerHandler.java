package xyz.usepontual.pontual.appointment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppointmentControllerHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(TimeAlreadyScheduledException.class)
    ProblemDetail handleTimeAlreadyScheduledException(TimeAlreadyScheduledException e, HttpServletRequest request) {
        logger.warn("Time slot overlaps with an existing appointment for this provider {}", request.getRequestURI(), e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }
}
