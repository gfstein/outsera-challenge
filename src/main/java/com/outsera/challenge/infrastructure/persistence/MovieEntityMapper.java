package com.outsera.challenge.infrastructure.persistence;

import com.outsera.challenge.domain.model.movie.Movie;
import com.outsera.challenge.domain.model.movie.MovieData;
import com.outsera.challenge.domain.model.producer.Producer;
import com.outsera.challenge.domain.model.studio.Studio;
import com.outsera.challenge.infrastructure.persistence.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovieEntityMapper {

    public Movie toDomain(MovieEntity entity) {
        return Movie.create(toMovieData(entity));
    }

    public MovieData toMovieData(MovieEntity entity) {
        return MovieData.builder()
                .year(entity.getYear())
                .title(entity.getTitle())
                .winner(entity.isWinner())
                .studios(entity.getStudioNames().stream().map(Studio::create).toList())
                .producers(entity.getProducerNames().stream().map(Producer::create).toList())
                .build();
    }

    public MovieEntity toEntity(Movie movie) {
        return MovieEntity.builder()
                .year(movie.year())
                .title(movie.title())
                .winner(movie.winner())
                .studioNames(movie.studios().stream().map(Studio::name).collect(Collectors.toSet()))
                .producerNames(movie.producers().stream().map(Producer::name).collect(Collectors.toSet()))
                .build();
    }
}
