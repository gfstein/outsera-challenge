package com.outsera.challenge.infrastructure.persistence.repository;

import com.outsera.challenge.infrastructure.persistence.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaMovieRepository extends JpaRepository<MovieEntity, UUID> {

    List<MovieEntity> findByWinnerTrue();
}
