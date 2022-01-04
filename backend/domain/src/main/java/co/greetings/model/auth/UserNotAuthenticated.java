package co.greetings.model.auth;

import lombok.Getter;

@Getter
public class UserNotAuthenticated extends RuntimeException {
    private final String code = "Auth01";
    private final String message = "User could not be authenticated";

    public UserNotAuthenticated(Exception e) {
        super(e);
    }
}
