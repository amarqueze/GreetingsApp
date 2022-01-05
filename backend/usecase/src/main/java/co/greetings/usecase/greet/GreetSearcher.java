package co.greetings.usecase.greet;

import co.greetings.model.greet.Greet;
import co.greetings.model.greet.GreetRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GreetSearcher {
    @NonNull GreetRepository greetRepository;

    public Mono<Greet> searchOne(String acronym) {
        return greetRepository.find(acronym);
    }
}
