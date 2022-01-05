package co.greetings.restcontroller.api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.greetings.model.user.Users;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import co.greetings.restcontroller.api.util.MessageHttpResponse;
import co.greetings.restcontroller.api.util.SuccessMessageHttpResponse;
import co.greetings.usecase.user.UsersSearcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus400;
import static co.greetings.restcontroller.api.util.HttpCodeErrorCommonMapper.toHttpStatus500;

import java.util.List;

@RestController
@Tag(name = "${apidoc.user.name}", description = "${apidoc.user.description}")
public class GetAllUsersGetController {
    @Autowired private UsersSearcher usersSearcher;
    private static final String API_PATH = "/api/users";
    private static final Logger LOG = LoggerFactory.getLogger(GetAllUsersGetController.class);

    @Operation(summary = "Get all Users")
    @GetMapping(
        value = API_PATH,
        produces = { "application/json" }
    )
    public Mono<ResponseEntity<MessageHttpResponse>> getUsers() {
        return usersSearcher.searchAll()
            .doOnSuccess(users -> registerLogSuccess(users, API_PATH))
            .doOnError(this::registerLogError)
            .map(users ->  {
                List<UserDtoResponse> usersDto = users.getListUsers(UserDtoResponse::toUserDtoResponse);
                MessageHttpResponse response = new SuccessMessageHttpResponse<>(usersDto);
                return ResponseEntity.ok().body(response);
            })
            .onErrorResume(IllegalArgumentException.class, ex -> toHttpStatus400(ex, API_PATH))
            .onErrorResume(NullPointerException.class, ex -> toHttpStatus400(ex, API_PATH))
            .onErrorResume(IllegalStateException.class, ex -> toHttpStatus400(ex, API_PATH))
            .onErrorResume(NotReadyRepository.class, ex -> toHttpStatus500(ex, API_PATH))
            .onErrorResume(NotCouldContinueOperation.class, ex -> toHttpStatus500(ex, API_PATH))
            .onErrorResume(Exception.class, ex -> toHttpStatus500(API_PATH));
    }

    private void registerLogError(Throwable error) {
        LOG.error(error.getMessage());
    }

    private void registerLogSuccess(Users users, String path) {
        LOG.info(
            "Completed Handler [Get Users {} size] Origin: {}",
            users.getSize(),
            path
        );
    }
}
