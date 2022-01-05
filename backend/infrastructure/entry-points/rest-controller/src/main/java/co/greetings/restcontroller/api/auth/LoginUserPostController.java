package co.greetings.restcontroller.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.greetings.model.auth.NotCreatedSession;
import co.greetings.model.auth.Session;
import co.greetings.model.auth.UserNotAuthenticated;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import co.greetings.restcontroller.api.util.MessageHttpResponse;
import co.greetings.restcontroller.api.util.SuccessMessageHttpResponse;
import co.greetings.usecase.auth.SessionLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus400;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus401;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus500;

@RestController
@Tag(name = "${apidoc.auth.name}", description = "${apidoc.auth.description}")
public class LoginUserPostController {
    @Autowired private SessionLogin sessionLogin;
    private static final String API_PATH = "/api/auth";
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserPostController.class);

    @Operation(summary = "${apidoc.auth.operations.login}")
    @PostMapping(
        value = API_PATH,
        produces = { "application/json" }
    )
    public Mono<ResponseEntity<MessageHttpResponse>> addUser(@RequestBody CredentialsDtoRequest credentials) {
        return Mono.defer(() -> {
            var auth = credentials.toAuth();
            return sessionLogin.login(auth)
                .doOnSuccess(session -> registerLogSuccess(session, API_PATH))
                .doOnError(this::registerLogError)
                .map(session ->  {
                    MessageHttpResponse response = new SuccessMessageHttpResponse<>(SessionDtoResponse.toSessionDtoResponse(session));
                    return ResponseEntity.ok().body(response);
                });
        })
        .onErrorResume(IllegalArgumentException.class, ex -> toHttpStatus400(ex, API_PATH))
        .onErrorResume(NullPointerException.class, ex -> toHttpStatus400(ex, API_PATH))
        .onErrorResume(IllegalStateException.class, ex -> toHttpStatus400(ex, API_PATH))        
        .onErrorResume(UserNotAuthenticated.class, ex -> toHttpStatus401(ex, API_PATH))
        .onErrorResume(NotCreatedSession.class, ex -> toHttpStatus500(ex, API_PATH))  
        .onErrorResume(NotReadyRepository.class, ex -> toHttpStatus500(ex, API_PATH))   
        .onErrorResume(NotCouldContinueOperation.class, ex -> toHttpStatus500(ex, API_PATH))        
        .onErrorResume(Exception.class, ex -> toHttpStatus500(API_PATH));
    }

    private void registerLogError(Throwable error) {
        LOG.error(error.getMessage());
    }

    private void registerLogSuccess(Session session, String path) {
        LOG.info(
            "Completed Handler [Session {} created] Origin: {}",
            session.getFullname(),
            path
        );
    } 
}
