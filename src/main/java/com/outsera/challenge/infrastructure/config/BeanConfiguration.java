package com.outsera.challenge.infrastructure.config;

import com.outsera.challenge.application.usecase.GetProducerIntervalsUseCase;
import com.outsera.challenge.application.usecase.LoadMoviesUseCase;
import com.outsera.challenge.domain.gateway.MovieCsvReaderPort;
import com.outsera.challenge.domain.gateway.MovieRepositoryPort;
import com.outsera.challenge.domain.service.ProducerIntervalCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class BeanConfiguration {

    @Bean
    public ProducerIntervalCalculator producerIntervalCalculator() {
        return new ProducerIntervalCalculator();
    }

    @Bean
    public LoadMoviesUseCase loadMoviesUseCase(
            MovieCsvReaderPort movieCsvReaderPort,
            MovieRepositoryPort movieRepositoryPort
    ) {
        return new LoadMoviesUseCase(movieCsvReaderPort, movieRepositoryPort);
    }

    @Bean
    public GetProducerIntervalsUseCase getProducerIntervalsUseCase(
            MovieRepositoryPort movieRepositoryPort,
            ProducerIntervalCalculator producerIntervalCalculator
    ) {
        return new GetProducerIntervalsUseCase(movieRepositoryPort, producerIntervalCalculator);
    }
}
