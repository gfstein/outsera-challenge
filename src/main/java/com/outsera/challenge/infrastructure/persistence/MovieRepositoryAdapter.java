package com.outsera.challenge.infrastructure.persistence;

import com.outsera.challenge.domain.gateway.MovieRepositoryPort;
import com.outsera.challenge.domain.model.movie.Movie;
import com.outsera.challenge.infrastructure.persistence.repository.JpaMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieRepositoryAdapter implements MovieRepositoryPort {

    private final JpaMovieRepository jpaMovieRepository;
    private final MovieEntityMapper movieEntityMapper;

    @Override
    @Transactional
    public void saveAll(List<Movie> movies) {
        jpaMovieRepository.saveAll(movies.stream().map(movieEntityMapper::toEntity).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findWinners() {
        return jpaMovieRepository.findByWinnerTrue().stream()
                .map(movieEntityMapper::toDomain)
                .toList();
    }
}
