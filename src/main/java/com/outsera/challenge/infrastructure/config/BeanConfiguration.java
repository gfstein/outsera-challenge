package com.outsera.challenge.infrastructure.config;

import com.outsera.challenge.application.usecase.GetProducerIntervalsUseCase;
import com.outsera.challenge.application.usecase.LoadMoviesUseCase;
import com.outsera.challenge.domain.gateway.MovieCsvReaderPort;
import com.outsera.challenge.domain.gateway.MovieRepositoryPort;
import com.outsera.challenge.domain.service.ProducerIntervalCalculator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class BeanConfiguration {

    @Bean
    public OpenAPI goldenRaspberryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Golden Raspberry Awards API")
                        .description("Intervalos entre vitórias consecutivas dos produtores da categoria Pior Filme")
                        .version("1.0.0"));
    }

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
