package co.greetings.model.auth;

import lombok.Getter;

@Getter
public class UserNotAuthenticated extends RuntimeException {
    private final String code = "Auth01";
    private final String message = "User not authenticated";

    public UserNotAuthenticated() {
    }

    public UserNotAuthenticated(Exception e) {
        super(e);
    }
}
