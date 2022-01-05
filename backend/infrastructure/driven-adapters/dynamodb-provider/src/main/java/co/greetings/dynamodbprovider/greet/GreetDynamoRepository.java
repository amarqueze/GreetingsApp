package co.greetings.dynamodbprovider.greet;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.greetings.model.greet.Greet;
import co.greetings.model.greet.GreetRepository;
import co.greetings.model.greet.NotCreatedGreet;
import co.greetings.model.greet.NotFoundGreet;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

@Component
public class GreetDynamoRepository implements GreetRepository  {
    private static final String TABLE_NAME = "greetingsapp-greet";
    @Autowired private DynamoDbAsyncClient dbClient;

    @Override
    public Mono<Greet> add(Greet greet) {
        return Mono.error(new NotCreatedGreet());
    }

    @Override
    public Mono<Greet> find(String acronym) {
        return Mono.defer(() -> {
            var getItemRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("Acronym", AttributeValue.builder().s( acronym ).build()))
                .build();

            return Mono.fromCompletionStage(dbClient.getItem(getItemRequest))
                .flatMap(itemResponse -> {
                    if(!itemResponse.hasItem()) {
                        return Mono.error(new NotFoundGreet());
                    }
                    
                    return Mono.just(itemResponse);
                })
                .flatMap(itemResponse -> Mono.just( GreetMapper.fromMap(itemResponse.item())))
                .onErrorMap(SdkClientException.class, NotReadyRepository::new)           
                .onErrorMap(AwsServiceException.class, NotCouldContinueOperation::new);
        });
    }
    
}
