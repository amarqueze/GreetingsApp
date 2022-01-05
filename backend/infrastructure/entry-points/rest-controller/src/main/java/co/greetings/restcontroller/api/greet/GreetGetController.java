package co.greetings.restcontroller.api.greet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.greetings.model.auth.UserNotAuthenticated;
import co.greetings.model.greet.Greet;
import co.greetings.model.greet.NotFoundGreet;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import co.greetings.restcontroller.api.user.UserDtoResponse;
import co.greetings.restcontroller.api.util.MessageHttpResponse;
import co.greetings.usecase.greet.GreetSearcher;
import co.greetings.usecase.user.UserSearcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus400;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus401;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus500;

import java.security.Principal;

@RestController
@Tag(name = "${apidoc.greet.name}", description = "${apidoc.greet.description}")
@RequiredArgsConstructor
public class GreetGetController {
    private static final String API_PATH = "/api/greet";
    @Autowired  private GreetSearcher greatSearcher;
    @Autowired  private UserSearcher userSearcher;
    private final Logger log = LoggerFactory.getLogger(GreetGetController.class);

    @Operation(summary = "${apidoc.greet.operations.getgreetbylanguage}")
    @GetMapping(
        value = API_PATH,
        produces = {"application/json"}
    )
    public Mono<ResponseEntity<MessageHttpResponse>> getGreet(@RequestParam(defaultValue = "en") String lang, Principal principal) {
        return Mono.defer(() -> {
            var greetDtoResponse = new GreetDtoResponse<UserDtoResponse>();
            return greatSearcher.searchOne(lang)
                .doOnSuccess(greet -> registerLogSuccess(greet, API_PATH))
                .doOnError(this::registerLogError)
                .flatMap(greet -> {
                    greetDtoResponse.messageGreet = greet.getMessage();
                    return Mono.just(greetDtoResponse);
                })
                .flatMap(greetDto -> userSearcher.searchOne(principal.getName()))
                .map(user -> {
                    greetDtoResponse.data = UserDtoResponse.toUserDtoResponse(user);
                    return ResponseEntity.ok().body((MessageHttpResponse)greetDtoResponse);
                })
                .onErrorResume(IllegalArgumentException.class, ex -> toHttpStatus400(ex, API_PATH))
                .onErrorResume(NullPointerException.class, ex -> toHttpStatus400(ex, API_PATH))
                .onErrorResume(IllegalStateException.class, ex -> toHttpStatus400(ex, API_PATH))        
                .onErrorResume(UserNotAuthenticated.class, ex -> toHttpStatus401(ex, API_PATH))
                .onErrorResume(NotFoundGreet.class, ex -> toHttpStatus500(ex, API_PATH))
                .onErrorResume(NotReadyRepository.class, ex -> toHttpStatus500(ex, API_PATH))   
                .onErrorResume(NotCouldContinueOperation.class, ex -> toHttpStatus500(ex, API_PATH))        
                .onErrorResume(Exception.class, ex -> toHttpStatus500(API_PATH));
        });    
    }

    private void registerLogError(Throwable error) {
        log.error(error.getMessage());
    }

    private void registerLogSuccess(Greet session, String path) {
        log.info(
            "Completed Handler [Greet {} created] Origin: {}",
            session.getAcronym(),
            path
        );
    } 
}
