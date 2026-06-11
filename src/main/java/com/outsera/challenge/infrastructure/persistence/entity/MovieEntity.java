package com.outsera.challenge.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity extends BaseEntity {

    @Column(name = "movie_year", nullable = false)
    private int year;

    @Column(nullable = false)
    private String title;

    @Column(name = "is_winner", nullable = false)
    private boolean winner;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "producer_names", nullable = false)
    @Builder.Default
    private Set<String> producerNames = new HashSet<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "studio_names", nullable = false)
    @Builder.Default
    private Set<String> studioNames = new HashSet<>();
}
