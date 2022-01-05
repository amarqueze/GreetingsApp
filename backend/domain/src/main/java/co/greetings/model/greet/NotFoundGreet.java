package co.greetings.model.greet;

import lombok.Getter;

@Getter
public class NotFoundGreet extends RuntimeException {
    private final String code = "Greet02";
    private final String message = "Greet could not be found";

    public NotFoundGreet() {
    }

    public NotFoundGreet(Exception e) {
        super(e);
    }
}
