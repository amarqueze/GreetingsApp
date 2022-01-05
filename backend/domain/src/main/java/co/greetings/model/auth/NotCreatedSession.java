package co.greetings.model.auth;

import lombok.Getter;

@Getter
public class NotCreatedSession extends RuntimeException {
    private final String code = "Auth02";
    private final String message = "Not could be generated the token";

    public NotCreatedSession(Exception e) {
        super(e);
    }
}
