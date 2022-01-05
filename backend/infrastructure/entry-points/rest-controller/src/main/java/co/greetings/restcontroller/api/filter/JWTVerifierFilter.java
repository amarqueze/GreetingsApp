package co.greetings.restcontroller.api.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import co.greetings.model.auth.UserNotAuthenticated;
import co.greetings.usecase.auth.SessionSearcher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JWTVerifierFilter implements ServerSecurityContextRepository {
    @NonNull private SessionSearcher sessionSearcher;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .flatMap(authHeader -> {
                String jwt = authHeader.substring(7);
                return sessionSearcher.search(jwt);
            })
            .flatMap(session -> {
                Authentication auth = 
                    new UsernamePasswordAuthenticationToken(session.getEmailUser(), session.getToken(), null);
                return Mono.just((SecurityContext)new SecurityContextImpl(auth));
            })
            .onErrorResume(UserNotAuthenticated.class, ex -> Mono.empty());
    }    
}
