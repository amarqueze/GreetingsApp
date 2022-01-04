package co.greetings.dynamodbprovider.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.greetings.model.user.Role;
import co.greetings.model.user.User;
import co.greetings.model.user.Users;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class UserMapper {
    private UserMapper() {
    }

    public static Users fromList(List<Map<String, AttributeValue>> items) {
        List<User> listUsers = items.stream()
            .map(UserMapper::fromMap)
            .collect(Collectors.toList());

        return new Users(listUsers);
    }

    public static User fromMap(Map<String, AttributeValue> attributeValueMap) {
        return User.builder()
            .fullname( attributeValueMap.get("Fullname").s() )
            .email( attributeValueMap.get("Email").s() )
            .aboutMe( attributeValueMap.get("AboutMe").s() )
            .password( attributeValueMap.get("Password").s() )
            .role( new Role(attributeValueMap.get("Role").s()) )
            .build();
    }

    public static Map<String, AttributeValue> toMap(User user) {
        return Map.of(
            "Email", AttributeValue.builder().s( user.getEmail() ).build(),
            "Fullname", AttributeValue.builder().s( user.getFullname() ).build(),
            "AboutMe", AttributeValue.builder().n( user.getAboutMe() ).build(),
            "Password", AttributeValue.builder().s( user.getPassword() ).build(),
            "Role", AttributeValue.builder().s( user.getRole().getName() ).build()
        );
    }    
}
