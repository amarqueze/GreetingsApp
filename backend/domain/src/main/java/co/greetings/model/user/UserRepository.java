package co.greetings.model.user;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> add(User newUser);
    Mono<User> find(String emailUser);
    Mono<Users> getAllUsers();
}
