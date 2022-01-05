package co.greetings.usecase.user;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.greetings.model.user.DuplicateUser;
import co.greetings.model.user.NotCreatedUser;
import co.greetings.model.user.NotFoundUser;
import co.greetings.model.user.NotPermitOperation;
import co.greetings.model.user.Role;
import co.greetings.model.user.User;
import co.greetings.model.user.UserRepository;
import co.greetings.model.util.exceptions.NotReadyRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Test Cases: UserCreator")
@ContextConfiguration(classes = {UserCreator.class})
@ExtendWith(SpringExtension.class)
class UserCreatorTest {
    public static final String GENERIC_MESSAGE = "GENERIC_MESSAGE";  
    @MockBean
    UserRepository userRepository;
    @Autowired
    UserCreator userCreator;

    User user;
    User userAdmin;

    @BeforeEach
    public void init() {
        user = User.newUser(
            "Berton Suarez", 
            "bertonsuarez@gmail.com", 
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "qwerty123456", 
            new Role("Client")
        );

        userAdmin = User.newUser(
            "Joe Doe", 
            "joedoe@gmail.com", 
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "123456", 
            new Role("Admin")
        );
    }

    @Test
    @DisplayName("should created one user")
    void test1() {
        Mockito.when(userRepository.add(any(User.class)))
            .thenReturn(Mono.just(user)); 
            
        Mockito.when(userRepository.find(userAdmin.getEmail()))
            .thenReturn(Mono.just(userAdmin));
            
        Mockito.when(userRepository.find(user.getEmail()))
            .thenReturn(Mono.error(new NotFoundUser()));

        Mono<User> result = userCreator.create(user, "joedoe@gmail.com");
        StepVerifier.create(result)
            .expectNextMatches(newUser -> newUser.getEmail().equals("bertonsuarez@gmail.com"))
            .verifyComplete(); 
    }

    @Test
    @DisplayName("should got DuplicateUser Exception")
    void test2() {
        Mockito.when(userRepository.add(any(User.class)))
            .thenReturn(Mono.just(user)); 

        Mockito.when(userRepository.find(userAdmin.getEmail()))
            .thenReturn(Mono.just(userAdmin));
            
        Mockito.when(userRepository.find(user.getEmail()))
            .thenReturn(Mono.just(user));

        Mono<User> result = userCreator.create(user, "joedoe@gmail.com");
        StepVerifier.create(result)
            .consumeErrorWith(error -> assertThat(error, Matchers.instanceOf(DuplicateUser.class)))
            .verify(); 
    }

    @Test
    @DisplayName("should got NotPermitOperation Exception")
    void test3() {
        Mockito.when(userRepository.add(any(User.class)))
            .thenReturn(Mono.just(user)); 

        userAdmin = User.newUser(
            "Joe Doe", 
            "joedoe@gmail.com", 
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "123456", 
            new Role("Client")
        );
            
        Mockito.when(userRepository.find(userAdmin.getEmail()))
            .thenReturn(Mono.just(userAdmin));
            
        Mockito.when(userRepository.find(user.getEmail()))
            .thenReturn(Mono.just(user));

        Mono<User> result = userCreator.create(user, "joedoe@gmail.com");
        StepVerifier.create(result)
            .consumeErrorWith(error -> assertThat(error, Matchers.instanceOf(NotPermitOperation.class)))
            .verify(); 
    }

    @Test
    @DisplayName("should got NotCreatedUser Exception")
    void test4() {
        Mockito.when(userRepository.find(any(String.class)))
            .thenReturn(Mono.error(new NotCreatedUser(new Exception())));

        Mono<User> result = userCreator.create(user, "joedoe@gmail.com");
        StepVerifier.create(result)
            .consumeErrorWith(error -> assertThat(error, Matchers.instanceOf(NotCreatedUser.class)))
            .verify(); 
    }

    @Test
    @DisplayName("should got NotReadyRepository Exception")
    void test5() {
        Mockito.when(userRepository.find(any(String.class)))
            .thenReturn(Mono.error(new NotReadyRepository(new Exception())));

        Mono<User> result = userCreator.create(user, "joedoe@gmail.com");
        StepVerifier.create(result)
            .consumeErrorWith(error -> assertThat(error, Matchers.instanceOf(NotReadyRepository.class)))
            .verify(); 
    }
}
