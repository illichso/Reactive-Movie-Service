package com.example.demo.rest;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;
import com.example.demo.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/{id}/events", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEvent> events(@PathVariable String id) {
        Mono<Movie> monoMovie = movieService.byId(id);
        return
                monoMovie.flatMap(movie -> monoMovie.ofType(Movie.class));
    }

    @GetMapping
    public Flux<Movie> all() {
        return movieService.all();
    }

    @GetMapping("/{id}")
    public Mono<Movie> byId(@PathVariable String id) {
        return movieService.byId(id);
    }
}
