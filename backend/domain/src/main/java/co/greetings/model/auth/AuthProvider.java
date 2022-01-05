package co.greetings.model.auth;

import reactor.core.publisher.Mono;

public interface AuthProvider {
    Mono<Session> add(String fullname, String emailUser);
    Mono<Session> find(String token);
}
