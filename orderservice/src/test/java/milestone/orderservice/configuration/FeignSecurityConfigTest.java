package milestone.orderservice.configuration;

import static org.junit.jupiter.api.Assertions.*;
// no mocks required

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;

public class FeignSecurityConfigTest {

    @Test
    void interceptor_forwardsBearer_whenJwtPresent() {
        FeignSecurityConfig cfg = new FeignSecurityConfig();
        var interceptor = cfg.bearerForwardingInterceptor();

    Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg","none"), Map.of("sub","user"));
        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(auth);

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);
        assertTrue(template.headers().containsKey("Authorization"));
        SecurityContextHolder.clearContext();
    }
}
