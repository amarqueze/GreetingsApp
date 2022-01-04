package co.greetings.model.user;

import static co.greetings.model.util.Preconditions.checkArgument;

import co.greetings.model.util.StringUtils;
import lombok.Getter;

@Getter
public class Role {
    private String name;

    public Role(String name) {
        validateFieldEmpty(name, "Name role is required");
        this.name = name;
    }

    private void validateFieldEmpty(String field, String messageExeception) {
        checkArgument(StringUtils::isBlank, field, messageExeception);
    }
}
