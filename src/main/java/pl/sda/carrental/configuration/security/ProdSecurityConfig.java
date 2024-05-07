package pl.sda.carrental.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class ProdSecurityConfig {

    private static final String[] WHITELIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(Customizer.withDefaults());

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(authorizationMatcher ->
                authorizationMatcher
                        .requestMatchers(WHITELIST).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/auth/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/public/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/authenticated/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/authenticated/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/authenticated/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/authenticated/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/authenticated/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/manageL1/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/manageL1/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/manageL1/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/manageL1/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/manageL1/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET, "/api/manageL2/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/manageL2/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/manageL2/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/manageL2/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/manageL2/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        .anyRequest()
                        .authenticated());

        return httpSecurity.build();
    }
}
