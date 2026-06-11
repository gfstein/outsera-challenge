package com.outsera.challenge.presentation.rest.advice;

import com.outsera.challenge.domain.exception.InvalidMovieDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidMovieDataException.class)
    public ProblemDetail handleInvalidMovieData(InvalidMovieDataException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
