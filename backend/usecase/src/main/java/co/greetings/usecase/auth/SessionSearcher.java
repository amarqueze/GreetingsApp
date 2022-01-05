package co.greetings.usecase.auth;

import co.greetings.model.auth.AuthProvider;
import co.greetings.model.auth.Session;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SessionSearcher {
    @NonNull AuthProvider authProvider;

    public Mono<Session> search(String token) {
        return authProvider.find(token);            
    }
}
