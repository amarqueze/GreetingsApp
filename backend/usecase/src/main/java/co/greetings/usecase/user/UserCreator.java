package co.greetings.usecase.user;

import co.greetings.model.user.DuplicateUser;
import co.greetings.model.user.NotFoundUser;
import co.greetings.model.user.NotPermitOperation;
import co.greetings.model.user.User;
import co.greetings.model.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserCreator {
    @NonNull UserRepository userRepository;

    public Mono<User> create(User newUser, String byEmailUser) {
        return checkRole(byEmailUser)
            .switchIfEmpty(createUser(newUser));
    }

    private Mono<User> checkRole(String byEmailUser) {
        return userRepository.find(byEmailUser)
            .handle((user, chain) -> {                
                if( user.getRole().getName().equals("Admin") ) {
                    chain.complete();
                } else {
                    chain.error(new NotPermitOperation());
                }
            });
    } 

    private Mono<User> createUser(User newUser) {
        return Mono.defer(() -> userRepository.find(newUser.getEmail())
            .hasElement()
            .onErrorReturn(NotFoundUser.class, false)
            .flatMap(existUser -> {
                if(!existUser.booleanValue()) {
                    return userRepository.add(newUser);                    
                } else {
                    return Mono.error(new DuplicateUser());
                }
            })
        );
    }
}
