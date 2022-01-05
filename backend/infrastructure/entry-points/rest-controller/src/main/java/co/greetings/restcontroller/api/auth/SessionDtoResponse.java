package co.greetings.restcontroller.api.auth;

import co.greetings.model.auth.Session;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SessionDtoResponse {
    private String fullname;
    private String emailUser;    
    private String token;

    public static SessionDtoResponse toSessionDtoResponse(Session session) {
        return SessionDtoResponse.builder()
            .emailUser(session.getEmailUser())
            .fullname(session.getFullname())
            .token(session.getToken())
            .build();
    }
}
