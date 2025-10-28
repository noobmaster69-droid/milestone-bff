package milestone.orderservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import milestone.orderservice.client.ProductClient;
import milestone.orderservice.client.ProductDto;

import milestone.orderservice.dto.OrderResponse;
import milestone.orderservice.dto.CreateOrderRequest;
import milestone.orderservice.model.Order;
import milestone.orderservice.model.Payment;
import milestone.orderservice.repository.OrderRepository;
import milestone.orderservice.repository.PaymentRepository;

@Service
public class OrdersService {
    private final Logger logger = LoggerFactory.getLogger(OrdersService.class);
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductClient productClient;

    public OrdersService(OrderRepository orderRepository, PaymentRepository paymentRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productClient = productClient;
    }

    @Transactional
    public OrderResponse createOrder(Integer userId, CreateOrderRequest req) {
        if (req == null || req.productId == null) throw new IllegalArgumentException("productId required");
        if (req.quantity == null || req.quantity <= 0) throw new IllegalArgumentException("invalid quantity");
        ProductDto p = productClient.getById(req.productId.intValue());
        if (p == null) throw new IllegalArgumentException("product not found: " + req.productId);

        BigDecimal total = p.getBasePrice().multiply(BigDecimal.valueOf(req.quantity));

        Order order = new Order();
        order.setProductId(req.productId);
        order.setQuantity(req.quantity);
        order.setTotalPrice(total);
        order.setStatus("PENDING");
        order.setUserId(userId);
        order = orderRepository.save(order);

    logger.info("Order {} created for product={} qty={} total={} user={}", order.getOrderId(), req.productId, req.quantity, total, userId);

        // Payment attempt (dummy)
        boolean success = Math.random() < 0.5;
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setAmount(total);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus(success ? "SUCCESS" : "FAILED");
        paymentRepository.save(payment);

        if (success) {
            order.setStatus("CONFIRMED");
            orderRepository.save(order);
            logger.info("Payment for order {} succeeded txId={}", order.getOrderId(), payment.getTransactionId());
        } else {
            order.setStatus("PAYMENT_FAILED");
            orderRepository.save(order);
            logger.info("Payment for order {} failed txId={}", order.getOrderId(), payment.getTransactionId());
        }

        // build response
        OrderResponse resp = new OrderResponse();
        resp.orderId = order.getOrderId();
        resp.productId = order.getProductId();
        resp.quantity = order.getQuantity();
        resp.status = order.getStatus();
        resp.totalPrice = order.getTotalPrice();
        resp.createdAt = order.getCreatedAt();

        return resp;
    }

    @Transactional
    public List<OrderResponse> createOrders(Integer userId, List<CreateOrderRequest> reqs) {
        List<OrderResponse> out = new java.util.ArrayList<>();
        for (CreateOrderRequest r : reqs) {
            out.add(createOrder(userId, r));
        }
        return out;
    }

    public List<OrderResponse> getAllOrders(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if(orders.isEmpty()){OrderResponse resp = new OrderResponse();
        resp.orderId = 0L;
        resp.productId = 0L;
        resp.quantity = 0;
        resp.status = "NoOrderPlaced";
        resp.totalPrice = BigDecimal.ZERO;
        resp.createdAt = LocalDateTime.now();
        List<OrderResponse> output = new ArrayList<>();
        output.add(resp);
        return output;}
        List<OrderResponse> output = new ArrayList<>();
        for(Order order: orders){
            OrderResponse resp = new OrderResponse();
            resp.orderId = order.getOrderId();
            resp.productId = order.getProductId();
            resp.quantity = order.getQuantity();
            resp.status = order.getStatus();
            resp.totalPrice = order.getTotalPrice();
            resp.createdAt = order.getCreatedAt();
        }
        return output;
    }

    public OrderResponse getOrderForUser(Integer userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        if (order.getUserId() == null || !order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("access denied for order: " + orderId);
        }
        OrderResponse resp = new OrderResponse();
        resp.orderId = order.getOrderId();
        resp.productId = order.getProductId();
        resp.quantity = order.getQuantity();
        resp.status = order.getStatus();
        resp.totalPrice = order.getTotalPrice();
        resp.createdAt = order.getCreatedAt();
        return resp;
    }

    public List<OrderResponse> listOrdersForUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(o -> {
            OrderResponse r = new OrderResponse();
            r.orderId = o.getOrderId();
            r.productId = o.getProductId();
            r.quantity = o.getQuantity();
            r.status = o.getStatus();
            r.totalPrice = o.getTotalPrice();
            r.createdAt = o.getCreatedAt();
            return r;
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        OrderResponse resp = new OrderResponse();
        resp.orderId = order.getOrderId();
        resp.productId = order.getProductId();
        resp.quantity = order.getQuantity();
        resp.status = order.getStatus();
        resp.totalPrice = order.getTotalPrice();
        resp.createdAt = order.getCreatedAt();
        return resp;
    }

    public List<OrderResponse> listAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(o -> {
            OrderResponse r = new OrderResponse();
            r.orderId = o.getOrderId();
            r.productId = o.getProductId();
            r.quantity = o.getQuantity();
            r.status = o.getStatus();
            r.totalPrice = o.getTotalPrice();
            r.createdAt = o.getCreatedAt();
            return r;
        }).collect(Collectors.toList());
    }

    // using ProductDto from client package
}
