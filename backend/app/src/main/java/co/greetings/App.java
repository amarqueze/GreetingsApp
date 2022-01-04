package co.greetings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import java.time.Duration;


@SpringBootApplication(scanBasePackages={"co.greetings"})
public class App implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(App.class);
    @Value("${spring.application.name}") private String nameApp;
    @Value("${spring.application.version}") private String versionApp;
    @Value("${spring.application.description}") private String descriptionApp;

    @Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "HEAD");
			}
		};
	}

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf()
                .disable()
            .headers(headerCustomizer -> {
                headerCustomizer.xssProtection().disable();
                headerCustomizer.contentSecurityPolicy("frame-ancestors 'none'");
                headerCustomizer.hsts()
                    .includeSubdomains(true)
                    .maxAge(Duration.ofDays(365));
            });
        return http.build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
            .title(nameApp)
            .version(versionApp)
            .description(descriptionApp)
            .termsOfService("http://swagger.io/terms/")
            .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("RUNNING APPLICATION");
    }
}
