package co.greetings.model.greet;

import reactor.core.publisher.Mono;

public interface GreetRepository {
    Mono<Greet> add(Greet greet);
    Mono<Greet> find(String acronym);
}
