package co.greetings.restcontroller.api.auth;

import co.greetings.model.auth.Auth;
import lombok.Data;

@Data
public class CredentialsDtoRequest {
    private String emailUser;
    private String password;

    public Auth toAuth() {
        return new Auth(emailUser, password);
    }
}
