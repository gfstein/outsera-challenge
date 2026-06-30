package com.outsera.challenge.domain.model.shared;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class CsvNameListParser {

    private static final String DELIMITER = "|";
    private static final String DELIMITER_REGEX = "\\|";

    // Alternativas ordenadas: padrões mais longos primeiro para evitar match parcial.
    // Ex.: ", and " antes de " and " antes de ", "
    private static final Pattern SEPARATOR_PATTERN =
            Pattern.compile(", and | and | & |, |;");

    private static final Pattern TRAILING_COMMA = Pattern.compile(",$");

    public static <T> List<T> parse(String raw, Function<String, T> factory) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }

        return Arrays.stream(normalize(raw).split(DELIMITER_REGEX))
                .map(CsvNameListParser::cleanupToken)
                .filter(name -> !name.isEmpty())
                .map(factory)
                .toList();
    }

    private static String normalize(String raw) {
        return SEPARATOR_PATTERN.matcher(raw).replaceAll(DELIMITER);
    }

    private static String cleanupToken(String token) {
        // CSV usa ", and " (ex.: "Eric Fellner, and Tom Hooper"); após normalizar, sobra vírgula no fim do penúltimo nome
        return TRAILING_COMMA.matcher(token.trim()).replaceAll("");
    }
}
