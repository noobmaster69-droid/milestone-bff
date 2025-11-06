package milestone.orderservice.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;

public class ProductClientTest {

    @Test
    void interface_hasFeignClientAnnotation() {
        FeignClient ann = ProductClient.class.getAnnotation(FeignClient.class);
        assertNotNull(ann, "ProductClient should be annotated with @FeignClient");
        assertEquals("/products", ann.path());
    }
}
