package co.greetings.usecase.user;

import co.greetings.model.user.UserRepository;
import co.greetings.model.user.Users;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsersSearcher {
    @NonNull UserRepository userRepository;

    public Mono<Users> searchAll() {
        return userRepository.getAllUsers();
    }
}
