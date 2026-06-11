package com.outsera.challenge.infrastructure.config;

import com.outsera.challenge.application.usecase.LoadMoviesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieDataLoader implements ApplicationRunner {

    private final LoadMoviesUseCase loadMoviesUseCase;

    @Value("${movie.csv.path}")
    private String csvPath;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        int loaded = loadMoviesUseCase.execute(csvPath);
        log.info("Loaded {} movies from CSV", loaded);
    }
}
