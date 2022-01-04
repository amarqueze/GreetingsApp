package co.greetings.model.user;

import lombok.Getter;

@Getter
public class NotFoundUser extends RuntimeException {
    private final String code = "User02";
    private final String message = "User could not be found";

    public NotFoundUser(Exception e) {
        super(e);
    }
}
