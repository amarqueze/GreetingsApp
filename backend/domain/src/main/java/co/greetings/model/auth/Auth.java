package co.greetings.model.auth;

import co.greetings.model.util.StringUtils;
import lombok.Getter;

import static co.greetings.model.util.Preconditions.checkArgument;

@Getter
public class Auth {
    private String emailUser;
    private String password;

    public Auth(String emailUser, String password) {
        validateFieldEmpty(emailUser, "Email User is required");
        validateEmail(emailUser, "Email User is invalid");
        validateFieldEmpty(password, "Password is required");        

        this.emailUser = emailUser;
        this.password = encryptPassword(password);
    }

    private void validateFieldEmpty(String field, String messageExeception) {
        checkArgument(StringUtils::isBlank, field, messageExeception);
    }

    private void validateEmail(String email, String messageExeception) {
        checkArgument(s -> !StringUtils.isEmail(s), email, messageExeception);
    }

    private String encryptPassword(String passwordPlain) {
        try {
            return new String(StringUtils.encryptText(passwordPlain));
        } catch (Exception e) {
            throw new IllegalStateException("Password could not be encrypt");
        }
    }
}
