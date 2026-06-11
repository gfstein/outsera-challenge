package com.outsera.challenge.domain.model.movie;

import com.outsera.challenge.domain.model.producer.Producer;
import com.outsera.challenge.domain.model.studio.Studio;
import lombok.Builder;

import java.util.List;

@Builder
public record MovieData(
        int year,
        String title,
        List<Studio> studios,
        List<Producer> producers,
        boolean winner
) {
}
