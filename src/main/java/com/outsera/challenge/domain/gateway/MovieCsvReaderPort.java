package com.outsera.challenge.domain.gateway;

import com.outsera.challenge.domain.model.movie.Movie;

import java.util.List;

public interface MovieCsvReaderPort {

    List<Movie> read(String csvPath);
}
