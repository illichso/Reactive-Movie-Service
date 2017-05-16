package com.example.demo.rest;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;
import com.example.demo.service.MovieService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class FunctionalMovieController {

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
}
