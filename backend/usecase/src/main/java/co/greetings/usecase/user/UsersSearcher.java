package co.greetings.usecase.user;

import co.greetings.model.user.NotPermitOperation;
import co.greetings.model.user.UserRepository;
import co.greetings.model.user.Users;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsersSearcher {
    @NonNull UserRepository userRepository;

    public Mono<Users> searchAll(String byEmailUser) {
        return checkRole(byEmailUser)
            .switchIfEmpty(userRepository.getAllUsers());
    }

    private Mono<Users> checkRole(String byEmailUser) {
        return userRepository.find(byEmailUser)
            .handle((user, chain) -> {                
                if( user.getRole().getName().equals("Admin") ) {
                    chain.complete();
                } else {
                    chain.error(new NotPermitOperation());
                }
            });
    } 
}
