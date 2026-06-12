package com.outsera.challenge.integration;

import com.outsera.challenge.infrastructure.persistence.entity.MovieEntity;
import com.outsera.challenge.infrastructure.persistence.repository.JpaMovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieCsvImportIntegrationTest {

    @Autowired
    private JpaMovieRepository jpaMovieRepository;

    @Test
    void shouldLoad206MoviesFromCsv() {
        assertThat(jpaMovieRepository.count()).isEqualTo(206);
    }

    @Test
    void shouldLoad42WinnerMovies() {
        assertThat(jpaMovieRepository.findByWinnerTrue()).hasSize(42);
    }

    @Test
    void shouldCorrectlyParseMultipleProducersFromCsvCell() {
        // "Under the Cherry Moon" (1986): producers = "Bob Cavallo, Joe Ruffalo and Steve Fargnoli"
        MovieEntity movie = jpaMovieRepository.findAll().stream()
                .filter(m -> m.getYear() == 1986 && "Under the Cherry Moon".equals(m.getTitle()))
                .findFirst()
                .orElseThrow();

        assertThat(movie.isWinner()).isTrue();
        assertThat(movie.getProducerNames())
                .containsExactlyInAnyOrder("Bob Cavallo", "Joe Ruffalo", "Steve Fargnoli");
    }
}
