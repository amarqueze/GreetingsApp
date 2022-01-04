package co.greetings.model.greet;

import static co.greetings.model.util.Preconditions.checkArgument;

import co.greetings.model.util.StringUtils;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Greet {
    private String acronym;
    private String message;

    public Greet(String acronym, String message) {
        validateFieldEmpty(acronym, "Acronym is required");
        validateFieldAlphanumeric(acronym, "Acronym only must Alphanumeric characteres");
        validateFieldLength(acronym, 2, 4, "Acronym only must be 2 to 4 characteres");
        validateFieldEmpty(message, "Acronym is required");
        validateFieldLength(message, 1, 25, "Acronym only must be 1 to 25 characteres");

        this.acronym = acronym;
        this.message = message;
    }

    private void validateFieldEmpty(String field, String messageExeception) {
        checkArgument(StringUtils::isBlank, field, messageExeception);
    }

    private void validateFieldAlphanumeric(String fullname, String messageExeception) {
        checkArgument(s -> !StringUtils.isAlphanumeric(s), fullname, messageExeception);
    }

    private void validateFieldLength(String field, int minlength, int maxlength, String messageExeception) {
        checkArgument(s -> s.length() >= minlength && s.length() <= maxlength, field, messageExeception);
    }
}
