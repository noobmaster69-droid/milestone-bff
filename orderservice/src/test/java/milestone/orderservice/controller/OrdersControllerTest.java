package milestone.orderservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import milestone.orderservice.dto.CreateOrderRequest;
import milestone.orderservice.dto.OrderResponse;
import milestone.orderservice.service.OrdersService;

@ExtendWith(MockitoExtension.class)
public class OrdersControllerTest {

    @Mock
    OrdersService ordersService;

    @InjectMocks
    OrdersController controller;

    Jwt jwt;

    @BeforeEach
    void setup() {
        jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("userId")).thenReturn("10");
    }

    @Test
    void create_callsServiceWithUserId() {
        CreateOrderRequest r = new CreateOrderRequest();
        r.productId = 1L; r.quantity = 1;
        OrderResponse resp = new OrderResponse(); resp.orderId = 5L;
        when(ordersService.createOrders(eq(10), any())).thenReturn(List.of(resp));

        ResponseEntity<java.util.List<OrderResponse>> out = controller.create(jwt, List.of(r));
        assertEquals(200, out.getStatusCode().value());
        assertEquals(1, out.getBody().size());
    }

    @Test
    void getById_usesUserIdFromJwt() {
        OrderResponse resp = new OrderResponse(); resp.orderId = 33L;
        when(ordersService.getOrderForUser(10, 33L)).thenReturn(resp);
        ResponseEntity<OrderResponse> out = controller.getById(jwt, 33L);
        assertEquals(200, out.getStatusCode().value());
        assertEquals(33L, out.getBody().orderId.longValue());
    }

    @Test
    void getAllUsersOrders_callsService() {
        OrderResponse r = new OrderResponse(); r.orderId = 2L;
        when(ordersService.getAllOrders(10)).thenReturn(List.of(r));
        ResponseEntity<List<OrderResponse>> out = controller.getAllUsersOrders(jwt);
        assertEquals(200, out.getStatusCode().value());
        assertEquals(1, out.getBody().size());
    }
}
