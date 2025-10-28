package milestone.productservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import milestone.productservice.model.Product;
import milestone.productservice.repository.ProductRepository;
import milestone.productservice.dto.ProductResponse;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductRepository productRepository;

    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> resp = products.stream()
                .map(p -> new ProductResponse(p.getProductId(), p.getName(), p.getDescription(), p.getBasePrice(), p.getCategoryId(), p.getCreatedAt(), p.getUpdatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Integer id) {
        return productRepository.findById(id)
                .map(p -> new ProductResponse(p.getProductId(), p.getName(), p.getDescription(), p.getBasePrice(), p.getCategoryId(), p.getCreatedAt(), p.getUpdatedAt()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

