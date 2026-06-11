package com.outsera.challenge.infrastructure.csv;

import com.outsera.challenge.domain.exception.InvalidMovieDataException;
import com.outsera.challenge.domain.gateway.MovieCsvReaderPort;
import com.outsera.challenge.domain.model.movie.Movie;
import com.outsera.challenge.domain.model.movie.MovieData;
import com.outsera.challenge.domain.model.producer.Producer;
import com.outsera.challenge.domain.model.studio.Studio;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvMovieReaderAdapter implements MovieCsvReaderPort {

    private static final String HEADER_YEAR = "year";
    private static final String HEADER_TITLE = "title";
    private static final String HEADER_STUDIOS = "studios";
    private static final String HEADER_PRODUCERS = "producers";
    private static final String HEADER_WINNER = "winner";
    private static final String WINNER_FLAG = "yes";

    private static final CSVFormat MOVIE_CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader(HEADER_YEAR, HEADER_TITLE, HEADER_STUDIOS, HEADER_PRODUCERS, HEADER_WINNER)
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build();

    private final ResourceLoader resourceLoader;

    @Override
    public List<Movie> read(String csvPath) {
        List<Movie> movies = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(
                resourceLoader.getResource(csvPath).getInputStream(),
                StandardCharsets.UTF_8);
             CSVParser parser = MOVIE_CSV_FORMAT.parse(reader)) {

            for (CSVRecord csvRecord : parser) {
                movies.add(toMovie(csvRecord));
            }
        } catch (IOException exception) {
            throw new InvalidMovieDataException("Failed to read CSV at " + csvPath, exception);
        }

        return movies;
    }

    // Com mapper ficaria mais complexo, mas poderia ser uma boa opção para o futuro.
    private Movie toMovie(CSVRecord csvRecord) {
        try {
            return Movie.create(MovieData.builder()
                    .year(Integer.parseInt(csvRecord.get(HEADER_YEAR)))
                    .title(csvRecord.get(HEADER_TITLE))
                    .studios(Studio.fromCsvCell(csvRecord.get(HEADER_STUDIOS)))
                    .producers(Producer.fromCsvCell(csvRecord.get(HEADER_PRODUCERS)))
                    .winner(WINNER_FLAG.equalsIgnoreCase(csvRecord.get(HEADER_WINNER)))
                    .build());
        } catch (NumberFormatException exception) {
            throw new InvalidMovieDataException("Invalid year at CSV line " + csvRecord.getRecordNumber(), exception);
        }
    }
}
