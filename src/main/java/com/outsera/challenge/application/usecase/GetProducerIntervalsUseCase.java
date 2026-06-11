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
        return calculator.calculate(movieRepositoryPort.findWinners());
    }
}
