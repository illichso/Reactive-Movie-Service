package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {

    Flux<Movie> all();

    Mono<Movie> byId(String id);

    Flux<MovieEvent> streamStreams(Movie movie);
}
