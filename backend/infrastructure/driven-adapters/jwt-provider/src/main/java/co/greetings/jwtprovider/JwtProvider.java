package co.greetings.jwtprovider;

import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.greetings.model.auth.AuthProvider;
import co.greetings.model.auth.NotCreatedSession;
import co.greetings.model.auth.Session;
import co.greetings.model.auth.UserNotAuthenticated;
import reactor.core.publisher.Mono;

@Component
public class JwtProvider implements AuthProvider {
    @Autowired private RsaJsonWebKey rsaJsonWebKey;

    @Override
    public Mono<Session> add(String fullname, String emailUser) {
        return Mono.defer(() -> { 
            JwtClaims claims = new JwtClaims();
            claims.setIssuer("GreetingsApp"); 
            claims.setAudience("Technical Test"); 
            claims.setExpirationTimeMinutesInTheFuture(10);
            claims.setGeneratedJwtId();
            claims.setIssuedAtToNow();
            claims.setSubject(fullname); 
            claims.setClaim("email", emailUser);
            
            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setKey(rsaJsonWebKey.getPrivateKey());
            jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            String token = null;           
            try {                
                token = jws.getCompactSerialization();                
            } catch (JoseException e) {
                Mono.error(new NotCreatedSession(e));
            } 
            
            return Mono.just(Session.builder()
                .fullname(fullname)
                .emailUser(emailUser)
                .token(token)
                .build()
            );
        });
    }

    @Override
    public Mono<Session> find(String token) {
        return Mono.defer(() -> {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() 
            .setRequireSubject() 
            .setExpectedIssuer("GreetingsApp")
            .setExpectedAudience("Technical Test")
            .setVerificationKey(rsaJsonWebKey.getKey())
            .setJwsAlgorithmConstraints(
                    ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
            .build();

            String subject = null;
            String email = null;
            try {
                JwtClaims jwtClaims = jwtConsumer.processToClaims(token);  
                subject = jwtClaims.getClaimValueAsString("sub");
                email = jwtClaims.getClaimValueAsString("email");
            } catch (InvalidJwtException e) {
                return Mono.error(new UserNotAuthenticated(e));
            } 

            return Mono.just(Session.builder()
                .fullname(subject)
                .emailUser(email)
                .token(token)
                .build()
            );
        });
    }
    
}
