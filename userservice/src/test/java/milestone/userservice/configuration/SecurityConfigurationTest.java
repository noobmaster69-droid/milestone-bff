package milestone.userservice.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class SecurityConfigurationTest {

    @Test
    void jwtAuthenticationConverter_mapsAuthorities() {
        SecurityConfiguration cfg = new SecurityConfiguration();
        JwtAuthenticationConverter conv = cfg.jwtAuthenticationConverter();
        Jwt jwt = new Jwt("t", Instant.now(), Instant.now().plusSeconds(3600), Map.of("alg","HS256"), Map.of("authorities", List.of("ROLE_ADMIN")));
        Authentication auth = conv.convert(jwt);
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void jwtDecoder_and_encoder_notNull() {
        SecurityConfiguration cfg = new SecurityConfiguration();
        assertNotNull(cfg.jwtDecoder());
        assertNotNull(cfg.jwtEncoder());
    }
}
