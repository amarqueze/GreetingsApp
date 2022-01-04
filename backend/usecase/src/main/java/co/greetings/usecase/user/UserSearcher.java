package co.greetings.usecase.user;

import co.greetings.model.user.User;
import co.greetings.model.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserSearcher {
    @NonNull UserRepository userRepository;

    public Mono<User> searchOne(String emailUser) {
        return userRepository.find(emailUser);
    }
}
