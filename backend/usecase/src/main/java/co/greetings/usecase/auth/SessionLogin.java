package co.greetings.usecase.auth;

import co.greetings.model.auth.Auth;
import co.greetings.model.auth.AuthProvider;
import co.greetings.model.auth.Session;
import co.greetings.model.auth.UserNotAuthenticated;
import co.greetings.model.user.NotFoundUser;
import co.greetings.model.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SessionLogin {
    @NonNull UserRepository userRepository;
    @NonNull AuthProvider authProvider;

    public Mono<Session> login(Auth auth) {
        return userRepository.find(auth.getEmailUser())    
            .flatMap(user -> {
                if( user.getPassword().equals(auth.getPassword()) ) {
                    return Mono.just(user);
                } else {
                    return Mono.error(new UserNotAuthenticated());
                }
            })
            .flatMap(user -> authProvider.add(user.getFullname(), user.getEmail()) )
            .onErrorMap(NotFoundUser.class, ex -> new UserNotAuthenticated()); 
    }
}
