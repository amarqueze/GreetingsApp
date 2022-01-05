package co.greetings.restcontroller.api.user;

import co.greetings.model.user.User;
import co.greetings.model.user.Role;
import lombok.Data;

@Data
public class UserDtoRequest {
    private String fullname;
    private String email;
    private String aboutMe;
    private String role;
    private String password;

    public User toUser() {
        return User.newUser(
            fullname, 
            email, 
            aboutMe, 
            password, 
            new Role(role)
        );
    }
}
