package milestone.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
    import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import milestone.orderservice.dto.CreateOrderRequest;
import milestone.orderservice.dto.OrderResponse;
import milestone.orderservice.service.OrdersService;

public class OrderControllerAuthTest {

    private MockMvc mockMvc;

    @Mock
    OrdersService ordersService;

    @InjectMocks
    OrdersController ordersController;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(ordersController)
        .setCustomArgumentResolvers(new org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver())
        .build();
    }

    @Test
    void admin_can_access_list_all() throws Exception {
        when(ordersService.listAllOrders()).thenReturn(List.of());

        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none")
                .claim("authorities", List.of("ROLE_ADMIN")).build();
        Authentication auth = new JwtAuthenticationToken(jwt);

    SecurityContextHolder.getContext().setAuthentication(auth);
    mockMvc.perform(get("/orders").principal(auth)).andExpect(status().isOk());
    SecurityContextHolder.clearContext();
    }

    @Test
    void create_requires_authenticated_user() throws Exception {
    CreateOrderRequest r = new CreateOrderRequest();
    r.productId = 1L;
    r.quantity = 2;

        when(ordersService.createOrders(any(), any())).thenReturn(List.of(new OrderResponse()));

        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("userId", 5).build();
        Authentication auth = new JwtAuthenticationToken(jwt);

    SecurityContextHolder.getContext().setAuthentication(auth);
    mockMvc.perform(post("/orders").principal(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(List.of(r))))
        .andExpect(status().isOk());
    SecurityContextHolder.clearContext();
    }
}
