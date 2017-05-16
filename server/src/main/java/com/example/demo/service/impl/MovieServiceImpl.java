package com.example.demo.service.impl;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.MovieService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Date;
import java.util.Random;

import static java.time.Duration.ofSeconds;
import static java.util.stream.Stream.generate;
import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Flux.interval;
import static reactor.core.publisher.Flux.zip;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Flux<Movie> all() {
        return movieRepository.findAll();
    }

    public Mono<Movie> byId(String id) {
        return movieRepository.findById(id);
    }

    public Flux<MovieEvent> streamStreams(Movie movie) {
        Flux<Long> movieEventInterval = interval(ofSeconds(1));
        Flux<MovieEvent> events = fromStream(generate(() ->
                new MovieEvent(movie, new Date(), getRandomUser())));

        return zip(movieEventInterval, events).map(Tuple2::getT2);
    }

    private String getRandomUser() {
        String[] users = "user1,user2,user3,user4".split(",");
        return users[new Random().nextInt(users.length)];
    }
}
