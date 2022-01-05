package co.greetings.model.user;

import lombok.Getter;

@Getter
public class NotPermitOperation extends RuntimeException {
    private final String code = "User04";
    private final String message = "User cannot execute operation";
}
