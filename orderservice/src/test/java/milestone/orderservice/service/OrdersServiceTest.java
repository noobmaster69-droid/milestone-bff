package milestone.orderservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import milestone.orderservice.client.ProductClient;
import milestone.orderservice.client.ProductDto;
import milestone.orderservice.dto.CreateOrderRequest;
import milestone.orderservice.dto.OrderResponse;
import milestone.orderservice.model.Order;
import milestone.orderservice.repository.OrderRepository;
import milestone.orderservice.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    ProductClient productClient;

    @InjectMocks
    OrdersService service;

    @BeforeEach
    void setup() {
    }

    @Test
    void createOrder_happyPath() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.productId = 1L;
        req.quantity = 2;

    ProductDto p = mock(ProductDto.class);
    when(p.getBasePrice()).thenReturn(BigDecimal.valueOf(5));
    when(productClient.getById(1)).thenReturn(p);

        Order saved = new Order();
        saved.setOrderId(100L);
        saved.setProductId(1L);
        saved.setQuantity(2);
        saved.setTotalPrice(BigDecimal.valueOf(10));
        saved.setStatus("PENDING");
        saved.setCreatedAt(LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponse resp = service.createOrder(10, req);
        assertEquals(100L, resp.orderId.longValue());
        assertEquals(BigDecimal.valueOf(10), resp.totalPrice);
    }

    @Test
    void getOrderForUser_accessDenied() {
        Order o = new Order();
        o.setOrderId(5L);
        o.setUserId(2);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(o));
        assertThrows(IllegalArgumentException.class, () -> service.getOrderForUser(1, 5L));
    }

    @Test
    void listAllOrders_mapsCorrectly() {
        Order o = new Order();
        o.setOrderId(7L);
        o.setProductId(3L);
        o.setQuantity(1);
        o.setTotalPrice(BigDecimal.valueOf(2.5));
        o.setStatus("CONFIRMED");
        o.setCreatedAt(LocalDateTime.now());
        when(orderRepository.findAll()).thenReturn(List.of(o));
        List<OrderResponse> list = service.listAllOrders();
        assertEquals(1, list.size());
        assertEquals(7L, list.get(0).orderId.longValue());
    }

    @Test
    void createOrder_invalidQuantity_throws() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.productId = 1L;
        req.quantity = 0; // invalid
        assertThrows(IllegalArgumentException.class, () -> service.createOrder(1, req));
    }

    @Test
    void createOrders_multiple_invokesCreate() {
        CreateOrderRequest r1 = new CreateOrderRequest(); r1.productId = 1L; r1.quantity = 1;
        CreateOrderRequest r2 = new CreateOrderRequest(); r2.productId = 2L; r2.quantity = 2;
        ProductDto p1 = mock(ProductDto.class); when(p1.getBasePrice()).thenReturn(BigDecimal.valueOf(1));
        ProductDto p2 = mock(ProductDto.class); when(p2.getBasePrice()).thenReturn(BigDecimal.valueOf(2));
        when(productClient.getById(1)).thenReturn(p1);
        when(productClient.getById(2)).thenReturn(p2);

        Order saved1 = new Order(); saved1.setOrderId(11L); saved1.setProductId(1L); saved1.setQuantity(1); saved1.setTotalPrice(BigDecimal.valueOf(1)); saved1.setCreatedAt(LocalDateTime.now());
        Order saved2 = new Order(); saved2.setOrderId(12L); saved2.setProductId(2L); saved2.setQuantity(2); saved2.setTotalPrice(BigDecimal.valueOf(4)); saved2.setCreatedAt(LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(saved1).thenReturn(saved2);

        var out = service.createOrders(5, List.of(r1, r2));
        assertEquals(2, out.size());
    }

    @Test
    void getOrderById_notFound_throws() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getOrderById(999L));
    }

    @Test
    void getAllOrders_empty_returnsNoOrderPlaced() {
        when(orderRepository.findByUserId(42)).thenReturn(List.of());
        List<OrderResponse> out = service.getAllOrders(42);
        assertEquals(1, out.size());
        assertEquals("NoOrderPlaced", out.get(0).status);
    }

    @Test
    void listOrdersForUser_maps() {
        Order o = new Order();
        o.setOrderId(21L);
        o.setUserId(3);
        o.setProductId(4L);
        o.setQuantity(2);
        o.setTotalPrice(BigDecimal.valueOf(20));
        o.setStatus("CONFIRMED");
        o.setCreatedAt(LocalDateTime.now());
        when(orderRepository.findByUserId(3)).thenReturn(List.of(o));
        List<OrderResponse> out = service.listOrdersForUser(3);
        assertEquals(1, out.size());
        assertEquals(21L, out.get(0).orderId.longValue());
    }
}
