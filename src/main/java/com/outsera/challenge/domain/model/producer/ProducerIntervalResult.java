package com.outsera.challenge.domain.model.producer;

import java.util.List;

public record ProducerIntervalResult(
        List<ProducerAwardInterval> min,
        List<ProducerAwardInterval> max
) {
    public ProducerIntervalResult {
        min = List.copyOf(min);
        max = List.copyOf(max);
    }

    public static ProducerIntervalResult empty() {
        return new ProducerIntervalResult(List.of(), List.of());
    }

    public record ProducerAwardInterval(
            String producer,
            int interval,
            int previousWin,
            int followingWin
    ) {
    }
}
