package com.outsera.challenge.application.usecase;

import com.outsera.challenge.domain.gateway.MovieRepositoryPort;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult;
import com.outsera.challenge.domain.service.ProducerIntervalCalculator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProducerIntervalsUseCase {

    private final MovieRepositoryPort movieRepositoryPort;
    private final ProducerIntervalCalculator calculator;

    public ProducerIntervalResult execute() {
        // Candidato a cache (ex.: @Cacheable): dados imutáveis após startup, resultado sempre igual.
        return calculator.calculate(movieRepositoryPort.findWinners());
    }
}
