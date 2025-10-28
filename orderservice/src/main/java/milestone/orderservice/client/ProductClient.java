package milestone.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "productservice",
        path = "/products",
        configuration = milestone.orderservice.configuration.FeignSecurityConfig.class
)
public interface ProductClient {
    @GetMapping("/{id}")
    ProductDto getById(@PathVariable("id") Integer id);
}
