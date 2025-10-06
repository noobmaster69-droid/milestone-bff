package milestone.userservice.configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .httpBasic(Customizer.withDefaults()) // optional; remove if you want JWT-only
                .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String ut = jwt.getClaimAsString("usertype");
            if (ut == null) return List.of();
            return List.of(new SimpleGrantedAuthority("ROLE_" + ut.toUpperCase()));
        });
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Produces hashes like {bcrypt}$2a...
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager user(PasswordEncoder encoder){
        String encoded = encoder.encode("root"); // will be {bcrypt}$2a...
        return new InMemoryUserDetailsManager(
                User.withUsername("username")
                        .password(encoded)              // stores a bcrypt hash, prefixed with {bcrypt}
                        .authorities("read")
                        .build()
        );
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        String secret = "secretkeysecretkeysecretkeysecretkey"; // move to config
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

        // For HMAC signing, use ImmutableSecret as the JWKSource
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(secretBytes);
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = "secretkeysecretkeysecretkeysecretkey";
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(secretBytes, "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
