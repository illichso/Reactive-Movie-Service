package com.example.demo;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.Random;
import java.util.UUID;

import static java.util.stream.Stream.of;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class DemoApplication {

    private final MovieRepository movieRepository;

    @Autowired
    public DemoApplication(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Bean
    RouterFunction<?> routes(MovieService movieService) {
        return route(GET("/movies"),
                request -> ok().body(movieService.all(), Movie.class))
                .andRoute(GET("/movies/{id}"),
                        request -> ok().body(movieService.byId(request.pathVariable("id")), Movie.class))
                .andRoute(GET("/movies/{id}/events"),
                        request -> ok()
                                .contentType(TEXT_EVENT_STREAM)
                                .body(movieService.byId(request.pathVariable("id"))
                                        .flatMapMany(movieService::streamStreams), MovieEvent.class));
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
                .forEach(movie -> movieRepository.save(movie).subscribe(System.out::println));
    }

    private String randomGenre() {
        String[] genres = "genre1,genre2,genre3,genre4".split(",");
        return genres[new Random().nextInt(genres.length)];
    }

    public static void main(String[] args) {
        run(DemoApplication.class, args);
    }
}
