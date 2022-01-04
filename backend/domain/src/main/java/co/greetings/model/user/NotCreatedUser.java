package co.greetings.model.user;

import lombok.Getter;

@Getter
public class NotCreatedUser extends RuntimeException {
    private final String code = "User01";
    private final String message = "User could not be created";

    public NotCreatedUser(Exception e) {
        super(e);
    }
}
