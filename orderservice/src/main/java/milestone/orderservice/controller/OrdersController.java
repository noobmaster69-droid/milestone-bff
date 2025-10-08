package milestone.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;

import milestone.orderservice.dto.CreateOrderRequest;
import milestone.orderservice.dto.OrderResponse;
import milestone.orderservice.service.OrdersService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // Create accepts a list of orders for the authenticated user
    @PostMapping
    public ResponseEntity<java.util.List<OrderResponse>> create(@AuthenticationPrincipal Jwt jwt, @RequestBody java.util.List<CreateOrderRequest> reqs) {
        Integer userId = Integer.valueOf(jwt.getClaimAsString("userId"));
        java.util.List<OrderResponse> resp = ordersService.createOrders(userId, reqs);
        return ResponseEntity.ok(resp);
    }

    // Get a single order only if it belongs to the authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Long id) {
        Integer userId = Integer.valueOf(jwt.getClaimAsString("userId"));
        OrderResponse resp = ordersService.getOrderForUser(userId, id);
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/getAllMyOrders")
    public ResponseEntity<List<OrderResponse>> getAllUsersOrders(@AuthenticationPrincipal Jwt jwt) {
        Integer userId = Integer.valueOf(jwt.getClaimAsString("userId"));
        List<OrderResponse> resp = ordersService.getAllOrders(userId);
        return ResponseEntity.ok(resp);
    }

    // listAll restricted to admin role
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<OrderResponse>> listAll() {
        return ResponseEntity.ok(ordersService.listAllOrders());
    }
}
