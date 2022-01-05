package co.greetings.dynamodbprovider.greet;

import java.util.Map;

import co.greetings.model.greet.Greet;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class GreetMapper {
    private GreetMapper() {        
    }

    public static Greet fromMap(Map<String, AttributeValue> attributeValueMap) {
        return Greet.builder()
            .acronym( attributeValueMap.get("Acronym").s() )
            .message( attributeValueMap.get("Message").s() )
            .build();
    }
}
