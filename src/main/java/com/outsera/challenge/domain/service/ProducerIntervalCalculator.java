package com.outsera.challenge.domain.service;

import com.outsera.challenge.domain.model.movie.Movie;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult;
import com.outsera.challenge.domain.model.producer.ProducerIntervalResult.ProducerAwardInterval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProducerIntervalCalculator {

    public ProducerIntervalResult calculate(List<Movie> movies) {
        // Passo 1: agrupa anos de vitória por produtor (precisa ver todos os filmes antes de calcular intervalos)
        Map<String, SortedSet<Integer>> winsByProducer = groupWinYearsByProducer(movies);

        // Passo 2: calcula intervalos consecutivos e já encontra min/max em um único loop
        return buildResult(winsByProducer);
    }

    private Map<String, SortedSet<Integer>> groupWinYearsByProducer(List<Movie> movies) {
        Map<String, SortedSet<Integer>> map = new HashMap<>();
        for (Movie movie : movies) {
            for (var producer : movie.producers()) {
                map.computeIfAbsent(producer.name(), _ -> new TreeSet<>()).add(movie.year());
            }
        }
        return map;
    }

    private ProducerIntervalResult buildResult(Map<String, SortedSet<Integer>> winsByProducer) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        List<ProducerAwardInterval> minList = new ArrayList<>();
        List<ProducerAwardInterval> maxList = new ArrayList<>();

        for (var entry : winsByProducer.entrySet()) {
            String producer = entry.getKey();
            Integer prev = null;
            for (int year : entry.getValue()) {
                if (prev != null) {
                    int interval = year - prev;
                    ProducerAwardInterval item = new ProducerAwardInterval(producer, interval, prev, year);
                    if (interval < min) { min = interval; minList = new ArrayList<>(); minList.add(item); }
                    else if (interval == min) { minList.add(item); }
                    if (interval > max) { max = interval; maxList = new ArrayList<>(); maxList.add(item); }
                    else if (interval == max) { maxList.add(item); }
                }
                prev = year;
            }
        }

        if (minList.isEmpty()) {
            return ProducerIntervalResult.empty();
        }

        minList.sort(Comparator.comparing(ProducerAwardInterval::producer));
        maxList.sort(Comparator.comparing(ProducerAwardInterval::producer));

        return new ProducerIntervalResult(minList, maxList);
    }
}