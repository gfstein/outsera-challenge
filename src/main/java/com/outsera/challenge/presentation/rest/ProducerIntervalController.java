package com.outsera.challenge.presentation.rest;

import com.outsera.challenge.application.usecase.GetProducerIntervalsUseCase;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Producers", description = "Intervalos de prêmios por produtor")
@RestController
@RequestMapping("/v1/producers")
@RequiredArgsConstructor
public class ProducerIntervalController {

    private final GetProducerIntervalsUseCase getProducerIntervalsUseCase;

    @Operation(summary = "Menor e maior intervalo entre vitórias consecutivas")
    @ApiResponse(responseCode = "200", description = "Calculado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno ao processar os dados")
    @GetMapping(value = "/interval", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProducerIntervalResult getIntervals() {
        return getProducerIntervalsUseCase.execute();
    }
}
