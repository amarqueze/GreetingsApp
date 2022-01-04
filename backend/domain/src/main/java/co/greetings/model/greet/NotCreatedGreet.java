package co.greetings.model.greet;

import lombok.Getter;

@Getter
public class NotCreatedGreet extends RuntimeException {
    private final String code = "Greet01";
    private final String message = "Greet could not be created";

    public NotCreatedGreet(Exception e) {
        super(e);
    }
}
