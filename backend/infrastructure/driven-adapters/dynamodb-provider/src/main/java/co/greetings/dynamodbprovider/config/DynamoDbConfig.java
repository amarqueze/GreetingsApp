package co.greetings.dynamodbprovider.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
@Component
public class DynamoDbConfig {
    private final String urlEndpointLocal;

    public DynamoDbConfig(@Value("${dynamodb.urlEndpointLocal}") String urlEndpointLocal) {
        this.urlEndpointLocal = urlEndpointLocal;
    }

    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
            .credentialsProvider(() -> AwsBasicCredentials.create("accessKeyId", "secretAccessKey"))
            .endpointOverride(URI.create(urlEndpointLocal))
            .region(Region.US_EAST_1)
            .build();
    }
}
