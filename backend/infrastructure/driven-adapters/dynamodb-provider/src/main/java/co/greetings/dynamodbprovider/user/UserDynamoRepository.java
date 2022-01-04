package co.greetings.dynamodbprovider.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.greetings.model.user.NotCreatedUser;
import co.greetings.model.user.NotFoundUser;
import co.greetings.model.user.User;
import co.greetings.model.user.UserRepository;
import co.greetings.model.user.Users;
import co.greetings.model.util.exceptions.NotCouldContinueOperation;
import co.greetings.model.util.exceptions.NotReadyRepository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@Component
public class UserDynamoRepository implements UserRepository {
    private static final String TABLE_NAME = "greetingsapp-user";
    @Autowired private DynamoDbAsyncClient dbClient;

    @Override
    public Mono<User> add(User newUser) {
        return Mono.defer(() -> {
            var putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(UserMapper.toMap(newUser))
                .build();

            return Mono.fromCompletionStage(dbClient.putItem(putItemRequest))
                .flatMap(putItemResponse -> Mono.just(newUser))
                .onErrorMap(SdkClientException.class, NotReadyRepository::new)           
                .onErrorMap(AwsServiceException.class, NotCreatedUser::new); 
        });
    }

    @Override
    public Mono<User> find(String emailUser) {
        return Mono.defer(() -> {
            var getItemRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("Email", AttributeValue.builder().s( emailUser ).build()))
                .build();

            return Mono.fromCompletionStage(dbClient.getItem(getItemRequest))
                .flatMap(itemResponse -> {
                    if(!itemResponse.hasItem()) {
                        return Mono.error(new NotFoundUser());
                    }
                    
                    return Mono.just(itemResponse);
                })
                .flatMap(itemResponse -> Mono.just( UserMapper.fromMap(itemResponse.item())))
                .onErrorMap(SdkClientException.class, NotReadyRepository::new)           
                .onErrorMap(AwsServiceException.class, NotCouldContinueOperation::new);
        });
    }

    @Override
    public Mono<Users> getAllUsers() {
        return Mono.defer(() -> {
            var scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

            return Mono.fromCompletionStage(dbClient.scan(scanRequest))
                .flatMap(response -> Mono.just(response.items()))
                .flatMap(items -> Mono.just(UserMapper.fromList(items)))
                .onErrorMap(SdkClientException.class, NotReadyRepository::new)         
                .onErrorMap(AwsServiceException.class, NotCouldContinueOperation::new);
        });
    }    
}
