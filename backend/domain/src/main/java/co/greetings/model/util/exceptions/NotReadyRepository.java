package co.greetings.model.util.exceptions;

import lombok.Getter;

@Getter
public class NotReadyRepository extends RuntimeException  {
    private final String code = "General01";
    private final String message = "Repository not found or is not ready to handle the request";

    public NotReadyRepository(Exception e) {
        super(e);
    }
}
