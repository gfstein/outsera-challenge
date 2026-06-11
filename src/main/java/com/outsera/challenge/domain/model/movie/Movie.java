package com.outsera.challenge.domain.model.movie;

import com.outsera.challenge.domain.exception.InvalidMovieDataException;
import com.outsera.challenge.domain.model.producer.Producer;
import com.outsera.challenge.domain.model.studio.Studio;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Movie {

    int year;
    String title;
    List<Studio> studios;
    List<Producer> producers;
    boolean winner;

    public static Movie create(MovieData data) {
        if (data.year() <= 0) {
            throw new InvalidMovieDataException("Year must be positive");
        }
        if (data.title() == null || data.title().isBlank()) {
            throw new InvalidMovieDataException("Title is required");
        }
        if (data.studios() == null || data.studios().isEmpty()) {
            throw new InvalidMovieDataException("At least one studio is required");
        }
        if (data.producers() == null || data.producers().isEmpty()) {
            throw new InvalidMovieDataException("At least one producer is required");
        }

        return new Movie(
                data.year(),
                data.title(),
                List.copyOf(data.studios()),
                List.copyOf(data.producers()),
                data.winner()
        );
    }
}
