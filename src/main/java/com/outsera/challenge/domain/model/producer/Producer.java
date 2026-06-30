package com.outsera.challenge.domain.model.producer;

import com.outsera.challenge.domain.exception.InvalidMovieDataException;
import com.outsera.challenge.domain.model.shared.CsvNameListParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Value
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Producer {

    String name;

    public static Producer create(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidMovieDataException("Producer name is required");
        }

        return new Producer(name.trim());
    }

    public static List<Producer> fromCsvCell(String raw) {
        return applyMissingSurnamesAndCreate(CsvNameListParser.parse(raw, name -> name));
    }

    /**
     * O CSV lista produtores como "Sid and Marty Krofft and Jimmy Miller".
     * No split, "Sid" chega sem sobrenome (sem espaco), enquanto "Marty Krofft"
     * já traz o sobrenome compartilhado daquele grupo. Copiamos o sobrenome do
     * proximo nome da lista, que mantem a mesma ordem do texto original.
     */
    private static List<Producer> applyMissingSurnamesAndCreate(List<String> names) {
        List<Producer> result = new ArrayList<>(names.size());

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);

            if (!name.contains(" ") && i + 1 < names.size()) {
                String nextName = names.get(i + 1);

                if (nextName.contains(" ")) {
                    String surname = nextName.substring(nextName.lastIndexOf(' ') + 1);
                    name = name.trim() + " " + surname;
                }
            }

            result.add(Producer.create(name));
        }

        return result;
    }
}
