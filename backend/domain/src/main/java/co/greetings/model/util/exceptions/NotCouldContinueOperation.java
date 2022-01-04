package co.greetings.model.util.exceptions;

import lombok.Getter;

@Getter
public class NotCouldContinueOperation extends RuntimeException {
    private final String code = "General02";
    private final String message = "An unexpected logical error has occurred on the server";

    public NotCouldContinueOperation(Exception e) {
        super(e);
    }
}
