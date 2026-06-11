package com.outsera.challenge.domain.model.shared;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class CsvNameListParser {

    private static final String DELIMITER = "|";
    private static final String DELIMITER_REGEX = "\\|";

    public static <T> List<T> parse(String raw, Function<String, T> factory) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }

        return split(raw).stream()
                .map(CsvNameListParser::cleanupToken)
                .filter(name -> !name.isEmpty())
                .map(factory)
                .toList();
    }

    private static List<String> split(String raw) {
        if (containsListSeparator(raw)) {
            return Arrays.asList(normalizeListSeparators(raw).split(DELIMITER_REGEX));
        }

        return List.of(raw);
    }

    private static boolean containsListSeparator(String raw) {
        return raw.contains(", and ")
                || raw.contains(", ")
                || raw.contains(";")
                || raw.contains(" & ")
                || raw.contains(" and ");
    }

    private static String normalizeListSeparators(String raw) {
        return raw
                .replace(", and ", DELIMITER)
                .replace(" and ", DELIMITER)
                .replace(" & ", DELIMITER)
                .replace(", ", DELIMITER)
                .replace(";", DELIMITER);
    }

    private static String cleanupToken(String token) {
        // CSV usa ", and " (ex.: "Eric Fellner, and Tom Hooper"); após normalizar, sobra vírgula no fim do penúltimo nome
        return token.trim().replaceAll(",$", "");
    }
}
