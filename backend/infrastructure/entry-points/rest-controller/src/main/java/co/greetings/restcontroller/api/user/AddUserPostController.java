package co.greetings.restcontroller.api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.greetings.model.user.DuplicateUser;
import co.greetings.model.user.NotCreatedUser;
import co.greetings.model.user.User;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import co.greetings.restcontroller.api.util.MessageHttpResponse;
import co.greetings.restcontroller.api.util.SuccessMessageHttpResponse;
import co.greetings.usecase.user.UserCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus400;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus500;

@RestController
@Tag(name = "${apidoc.user.name}", description = "${apidoc.user.description}")
public class AddUserPostController {
    @Autowired private UserCreator userCreator;
    private static final String API_PATH = "/api/user";
    private static final Logger LOG = LoggerFactory.getLogger(AddUserPostController.class);

    @Operation(summary = "${apidoc.user.operations.newUser}")
    @PostMapping(
        value = API_PATH,
        produces = { "application/json" }
    )
    public Mono<ResponseEntity<MessageHttpResponse>> addUser(@RequestBody UserDtoRequest userBody) {
        return Mono.defer(() -> {
            var newUser = userBody.toUser();
            return userCreator.create(newUser)
                .doOnSuccess(user -> registerLogSuccess(user, API_PATH))
                .doOnError(this::registerLogError)
                .map(user ->  {
                    MessageHttpResponse response = new SuccessMessageHttpResponse<>(UserDtoResponse.toUserDtoResponse(user));
                    return ResponseEntity.ok().body(response);
                });
        })
        .onErrorResume(IllegalArgumentException.class, ex -> toHttpStatus400(ex, API_PATH))
        .onErrorResume(NullPointerException.class, ex -> toHttpStatus400(ex, API_PATH))
        .onErrorResume(IllegalStateException.class, ex -> toHttpStatus400(ex, API_PATH))
        .onErrorResume(DuplicateUser.class, ex -> toHttpStatus400(ex, API_PATH))  
        .onErrorResume(NotCreatedUser.class, ex -> toHttpStatus500(ex, API_PATH))
        .onErrorResume(NotReadyRepository.class, ex -> toHttpStatus500(ex, API_PATH))   
        .onErrorResume(NotCouldContinueOperation.class, ex -> toHttpStatus500(ex, API_PATH))        
        .onErrorResume(Exception.class, ex -> toHttpStatus500(API_PATH));
    }

    private void registerLogError(Throwable error) {
        LOG.error(error.getMessage());
    }

    private void registerLogSuccess(User user, String path) {
        LOG.info(
            "Completed Handler [User {} created] Origin: {}",
            user.getFullname(),
            path
        );
    } 
}
