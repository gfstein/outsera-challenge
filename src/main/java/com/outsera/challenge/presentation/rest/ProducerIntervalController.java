package com.outsera.challenge.presentation.rest;

import com.outsera.challenge.application.usecase.GetProducerIntervalsUseCase;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/producers")
@RequiredArgsConstructor
public class ProducerIntervalController {

    private final GetProducerIntervalsUseCase getProducerIntervalsUseCase;

    @GetMapping(value = "/interval", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProducerIntervalResult getIntervals() {
        return getProducerIntervalsUseCase.execute();
    }
}
