package milestone.productservice.configuration;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import org.junit.jupiter.api.Test;

public class OpenApiConfigTest {

    @Test
    void openAPI_hasBearerJwtScheme() {
        OpenApiConfig cfg = new OpenApiConfig();
        OpenAPI api = cfg.openAPI();
        assertNotNull(api);
        Components comps = api.getComponents();
        assertNotNull(comps.getSecuritySchemes().get("bearer-jwt"));
    }
}
