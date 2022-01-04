package co.greetings.restcontroller.api.user;

import co.greetings.model.user.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String fullname;
    private String email;
    private String aboutMe;
    private String role;

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
            .fullname(user.getFullname())
            .email(user.getEmail())
            .aboutMe(user.getAboutMe())
            .role(user.getRole().getName())
            .build();
    }
}
