package milestone.productservice.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class ResourceServerConfigTest {

    @Test
    void jwtDecoderBean_created() {
        ResourceServerConfig cfg = new ResourceServerConfig();
        JwtDecoder decoder = cfg.jwtDecoder();
        assertNotNull(decoder);
    }
}
