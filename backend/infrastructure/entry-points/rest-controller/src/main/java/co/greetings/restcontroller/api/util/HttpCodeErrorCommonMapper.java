package co.greetings.restcontroller.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import reactor.core.publisher.Mono;

public class HttpCodeErrorCommonMapper {
    private HttpCodeErrorCommonMapper() {
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus400(IllegalArgumentException e, String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setStatus(HttpStatus.BAD_REQUEST.value())
            .setTitle("Field specified is invalid")
            .setdetail(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(msg));
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus400(NullPointerException e, String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setStatus(HttpStatus.BAD_REQUEST.value())
            .setTitle("Field specified not be null")
            .setdetail(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(msg));
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus400(IllegalStateException e, String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setStatus(HttpStatus.BAD_REQUEST.value())
            .setTitle("Illegal status")
            .setdetail(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(msg));
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus500(NotReadyRepository e, String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setCode(e.getCode())
            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setTitle("Failed to connect to Database")
            .setdetail(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(msg));
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus500(NotCouldContinueOperation e, String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setCode(e.getCode())
            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setTitle("Unexpected error has interrupted this operation")
            .setdetail(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(msg));
    }

    public static Mono<ResponseEntity<MessageHttpResponse>> toHttpStatus500(String path) {
        MessageHttpResponse msg = ErrorMessageHttpResponse.builder()
            .setPath(path)
            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setTitle("Unexpected error has interrupted this operation")
            .setdetail("This information is restricted");
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(msg));
    }
}