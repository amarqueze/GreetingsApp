package co.greetings.usecase.user;

import co.greetings.model.user.User;
import co.greetings.model.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserCreator {
    @NonNull UserRepository userRepository;

    public Mono<User> create(User newUser) {
        return userRepository.add(newUser);
    }
}
