package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import static java.util.stream.Stream.of;

@Log
@Service
public class ServerMovieService {

    private final MovieRepository movieRepository;

    public ServerMovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Bean
    CommandLineRunner demo(MovieRepository movieRepository) {
        return args -> {
            movieRepository.deleteAll()
                    .subscribe(null, null, this::addMoviesToDB);
        };
    }

    private void addMoviesToDB() {
        of("Movie1", "Movie2", "Movie2", "Movie3")
                .map(name -> new Movie(UUID.randomUUID().toString(), name, randomGenre()))
                .forEach(movie -> movieRepository.save(movie)
                        .subscribe(movieEvent -> log.info(movieEvent.toString())));
    }

    private String randomGenre() {
        String[] genres = "genre1,genre2,genre3,genre4".split(",");
        return genres[new Random().nextInt(genres.length)];
    }
}
