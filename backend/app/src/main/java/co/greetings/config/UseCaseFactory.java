package co.greetings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.greetings.model.user.UserRepository;
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
}
