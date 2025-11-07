package milestone.productservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
// security annotations are not required here; controller is simple CRUD
import org.springframework.web.bind.annotation.RestController;

import milestone.productservice.model.Product;
import milestone.productservice.repository.ProductRepository;
import milestone.productservice.dto.ProductResponse;
import milestone.productservice.dto.UpdateStockRequest;

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
                .map(p -> new ProductResponse(p.getProductId(), p.getName(), p.getDescription(), p.getBasePrice(), p.getCategoryId(), p.getStock(), p.getCreatedAt(), p.getUpdatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Integer id) {
        return productRepository.findById(id)
                .map(p -> new ProductResponse(p.getProductId(), p.getName(), p.getDescription(), p.getBasePrice(), p.getCategoryId(), p.getStock(), p.getCreatedAt(), p.getUpdatedAt()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable("id") Integer id, @RequestBody UpdateStockRequest req) {
        return productRepository.findById(id).map(p -> {
            p.setStock(req.stock);
            Product saved = productRepository.save(p);
            ProductResponse resp = new ProductResponse(saved.getProductId(), saved.getName(), saved.getDescription(), saved.getBasePrice(), saved.getCategoryId(), saved.getStock(), saved.getCreatedAt(), saved.getUpdatedAt());
            return ResponseEntity.ok(resp);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

