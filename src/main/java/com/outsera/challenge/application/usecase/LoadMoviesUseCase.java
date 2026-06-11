package com.outsera.challenge.application.usecase;

import com.outsera.challenge.domain.gateway.MovieCsvReaderPort;
import com.outsera.challenge.domain.gateway.MovieRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadMoviesUseCase {

    private final MovieCsvReaderPort movieCsvReaderPort;
    private final MovieRepositoryPort movieRepositoryPort;

    public int execute(String csvPath) {
        var movies = movieCsvReaderPort.read(csvPath);
        movieRepositoryPort.saveAll(movies);
        return movies.size();
    }
}
