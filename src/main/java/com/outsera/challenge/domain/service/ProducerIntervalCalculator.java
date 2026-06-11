package com.outsera.challenge.domain.service;

import com.outsera.challenge.domain.model.movie.Movie;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult.ProducerAwardInterval;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProducerIntervalCalculator {

    public ProducerIntervalResult calculate(List<Movie> movies) {
        // CSV fixo (~200 filmes): agrega em memória; volume não justifica agregação no banco.
        List<ProducerAwardInterval> intervals = movies.stream()
                .flatMap(this::toProducerWins)
                .collect(groupingWinYearsByProducer())
                .entrySet().stream()
                .flatMap(entry -> toConsecutiveIntervals(entry.getKey(), entry.getValue()))
                .toList();

        return buildResult(intervals);
    }

    private Stream<ProducerWin> toProducerWins(Movie movie) {
        return movie.producers().stream()
                .map(producer -> new ProducerWin(producer.name(), movie.year()));
    }

    private Collector<ProducerWin, ?, Map<String, SortedSet<Integer>>> groupingWinYearsByProducer() {
        return Collectors.groupingBy(
                ProducerWin::producer,
                Collectors.mapping(
                        ProducerWin::year,
                        Collectors.toCollection(TreeSet::new)
                )
        );
    }

    private Stream<ProducerAwardInterval> toConsecutiveIntervals(String producer, SortedSet<Integer> winYears) {
        List<Integer> years = List.copyOf(winYears);

        return IntStream.range(1, years.size())
                .mapToObj(index -> new ProducerAwardInterval(
                        producer,
                        years.get(index) - years.get(index - 1),
                        years.get(index - 1),
                        years.get(index)
                ));
    }

    private ProducerIntervalResult buildResult(List<ProducerAwardInterval> intervals) {
        if (intervals.isEmpty()) {
            return ProducerIntervalResult.empty();
        }

        IntSummaryStatistics stats = intervals.stream()
                .mapToInt(ProducerAwardInterval::interval)
                .summaryStatistics();

        return new ProducerIntervalResult(
                filterByInterval(intervals, stats.getMin()),
                filterByInterval(intervals, stats.getMax())
        );
    }

    private List<ProducerAwardInterval> filterByInterval(List<ProducerAwardInterval> intervals, int interval) {
        return intervals.stream()
                .filter(awardInterval -> awardInterval.interval() == interval)
                .sorted(Comparator.comparing(ProducerAwardInterval::producer))
                .toList();
    }

    private record ProducerWin(String producer, int year) {
    }
}