package co.greetings.model.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Session {
    private String fullname;
    private String emailUser;    
    private String token;

    public Session(String fullname, String emailUser, String token) {
        this.fullname = fullname;
        this.emailUser = emailUser;
        this.token = token;
    }
}
