package xyz.usepontual.pontual.appointment;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@RestControllerAdvice
public class AppointmentControllerHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(TimeAlreadyScheduledException.class)
    ProblemDetail handleTimeAlreadyScheduledException(TimeAlreadyScheduledException e, HttpServletRequest request) {
        logger.warn("Time already scheduled {}", request.getRequestURI(), e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }
}
