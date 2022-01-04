package co.greetings.restcontroller.api.greet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "${apidoc.greet.name}", description = "${apidoc.greet.description}")
@RequiredArgsConstructor
public class GreetGetController {
    private static final String API_PATH = "/api/greet";

    private final Logger log = LoggerFactory.getLogger(GreetGetController.class);

    @Operation(summary = "${apidoc.greet.operations.getgreetbylanguage}")
    @GetMapping(
        value = API_PATH,
        produces = {"application/json"}
    )
    public Mono<ResponseEntity<String>> getGreetByLanguage() {        
        return Mono.just(ResponseEntity.ok().body("{\"greet\": \"Hello Word!!\"}"));
    }
}
