package com.example.demo;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.demo.model.Movie;
import com.example.demo.model.MovieEvent;

import static org.springframework.boot.SpringApplication.run;

@Log
@SpringBootApplication
public class ClientApplication {

    @Bean
    WebClient webClient() {
        return WebClient.create();
    }


    @Bean
    CommandLineRunner demo(WebClient webClient) {
        return args -> {

            String uriBase = "http://localhost:8080/movies";

            webClient
                    .get()
                    .uri(uriBase)
                    .exchange()
                    .flatMapMany(cr -> cr.bodyToFlux(Movie.class))
                    .filter(movie -> movie.getTitle().equalsIgnoreCase("Movie1"))
                    .subscribe(movie ->
                            webClient
                                    .get()
                                    .uri(uriBase + "/" + movie.getId() + "/events")
                                    .exchange()
                                    .flatMapMany(cr -> cr.bodyToFlux(MovieEvent.class))
                                    .subscribe(movieEvent -> log.info(movieEvent.toString())));

        };
    }

    public static void main(String[] args) {
        run(ClientApplication.class, args);
    }
}
