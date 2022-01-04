package co.greetings.model.user;

import static co.greetings.model.util.Preconditions.checkArgument;
import static co.greetings.model.util.Preconditions.checkNull;

import co.greetings.model.util.StringUtils;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private String fullname;
    private String email;
    private String aboutMe;
    private String password;
    private Role role;

    public static User newUser(String fullname, String email, String aboutMe, String password, Role role) {
        String cipherPassword;
        try {
            cipherPassword = new String(StringUtils.encryptText(password));            
        } catch (Exception e) {
            throw new IllegalStateException("Password could not be encrypt");
        }   

        return new User(fullname, email, aboutMe, cipherPassword, role);     
    }

    public User(String fullname, String email, String aboutMe, String password, Role role) {
        validateFieldEmpty(fullname, "Fullname is required");
        validateFieldLength(fullname, 3, 80, "Fullname only must be 3 to 80 characteres");
        validateFieldEmpty(email, "Email is required");
        validateEmail(email, "Email is invalid");
        validateFieldEmpty(aboutMe, "AboutMe is required");
        validateFieldLength(aboutMe, 3, 200, "aboutMe only must be 3 to 200 characteres");
        validateFieldEmpty(password, "Password is required");
        validateFieldLength(password, 6, 80, "Password only must be 3 to 80 characteres");
        checkNull(role, "Role is required");

        this.fullname = fullname;
        this.email = email;
        this.aboutMe = aboutMe;
        this.password = password;
        this.role = role;
    }

    private void validateFieldEmpty(String field, String messageExeception) {
        checkArgument(StringUtils::isBlank, field, messageExeception);
    }

    private void validateFieldLength(String field, int minlength, int maxlength, String messageExeception) {
        checkArgument(s -> s.length() < minlength || s.length() > maxlength, field, messageExeception);
    }

    private void validateEmail(String email, String messageExeception) {
        checkArgument(s -> !StringUtils.isEmail(s), email, messageExeception);
    }
}
