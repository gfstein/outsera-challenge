package com.outsera.challenge.domain.model.studio;

import com.outsera.challenge.domain.exception.InvalidMovieDataException;
import com.outsera.challenge.domain.model.shared.CsvNameListParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Studio {

    String name;

    public static Studio create(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidMovieDataException("Studio name is required");
        }

        return new Studio(name.trim());
    }

    public static List<Studio> fromCsvCell(String raw) {
        return CsvNameListParser.parse(raw, Studio::create);
    }
}
