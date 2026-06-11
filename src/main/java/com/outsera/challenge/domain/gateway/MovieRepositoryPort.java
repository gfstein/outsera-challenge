package com.outsera.challenge.domain.gateway;

import com.outsera.challenge.domain.model.movie.Movie;

import java.util.List;

public interface MovieRepositoryPort {

    void saveAll(List<Movie> movies);

    List<Movie> findWinners();
}
