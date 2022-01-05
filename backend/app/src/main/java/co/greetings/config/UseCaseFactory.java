package co.greetings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.greetings.model.auth.AuthProvider;
import co.greetings.model.greet.GreetRepository;
import co.greetings.model.user.UserRepository;
import co.greetings.usecase.auth.SessionLogin;
import co.greetings.usecase.auth.SessionSearcher;
import co.greetings.usecase.greet.GreetSearcher;
import co.greetings.usecase.user.UserCreator;
import co.greetings.usecase.user.UserSearcher;
import co.greetings.usecase.user.UsersSearcher;

@Configuration
public class UseCaseFactory {
    @Bean
    public UserCreator partnerCreator(UserRepository repository){
        return new UserCreator(repository);
    }

    @Bean
    public UserSearcher userSearcher(UserRepository repository){
        return new UserSearcher(repository);
    }

    @Bean
    public UsersSearcher usersSearcher(UserRepository repository){
        return new UsersSearcher(repository);
    }

    @Bean
    public SessionLogin sessionLogin(UserRepository repository, AuthProvider authProvider){
        return new SessionLogin(repository, authProvider);
    }

    @Bean
    public SessionSearcher sessionSearcher(AuthProvider authProvider){
        return new SessionSearcher(authProvider);
    }

    @Bean
    public GreetSearcher greetSearcher(GreetRepository repository){
        return new GreetSearcher(repository);
    }
}
