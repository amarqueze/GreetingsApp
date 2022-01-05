package co.greetings.model.user;

import lombok.Getter;

@Getter
public class DuplicateUser extends RuntimeException {
    private final String code = "User03";
    private final String message = "User email already created";
}
