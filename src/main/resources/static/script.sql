WITH RECURSIVE producer_split(movie_year, rest, producer) AS (
    SELECT movie_year,
           REGEXP_REPLACE(CAST(producer_names AS VARCHAR), '^\[|\]$', '') || ',',
           CAST(NULL AS VARCHAR)
    FROM movies
    WHERE is_winner = TRUE

    UNION ALL

    SELECT movie_year,
           SUBSTRING(rest FROM POSITION(',' IN rest) + 1),
           TRIM(BOTH '"' FROM TRIM(SUBSTRING(rest FROM 1 FOR POSITION(',' IN rest) - 1)))
    FROM producer_split
    WHERE POSITION(',' IN rest) > 0
),
               producer_wins(producer, movie_year) AS (
                   SELECT DISTINCT producer, movie_year
                   FROM producer_split
                   WHERE producer IS NOT NULL
                     AND producer <> ''
               ),
               win_intervals(producer, previous_win, following_win) AS (
                   SELECT producer,
                          LAG(movie_year) OVER (PARTITION BY producer ORDER BY movie_year),
                          movie_year
                   FROM producer_wins
               )
SELECT producer,
       following_win - previous_win AS award_interval,
       previous_win,
       following_win
FROM win_intervals
WHERE previous_win IS NOT NULL
ORDER BY award_interval, producer;