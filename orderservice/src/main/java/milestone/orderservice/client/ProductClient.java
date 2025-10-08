package milestone.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productservice", path = "/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable("id") Integer id);
}
