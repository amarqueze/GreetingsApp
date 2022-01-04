package co.greetings.model.user;

import static co.greetings.model.util.Preconditions.checkArgument;

import co.greetings.model.util.StringUtils;
import lombok.Getter;

@Getter
public class Role {
    private String name;

    public Role(String name) {
        validateFieldEmpty(name, "Name role is required");
        validateFieldAlphanumeric(name, "Name role only must Alphanumeric characteres");
        this.name = name;
    }

    private void validateFieldEmpty(String field, String messageExeception) {
        checkArgument(StringUtils::isBlank, field, messageExeception);
    }

    private void validateFieldAlphanumeric(String field, String messageExeception) {
        checkArgument(s -> !StringUtils.isAlphanumeric(s), field, messageExeception);
    }
}
