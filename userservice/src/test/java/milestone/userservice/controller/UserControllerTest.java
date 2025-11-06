package milestone.userservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import milestone.userservice.model.User;
import milestone.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtEncoder jwtEncoder;

    @InjectMocks
    UserController controller;

    User sampleUser;

    @BeforeEach
    void setup() {
        sampleUser = User.builder()
                .id(1)
                .username("alice")
                .email("alice@example.com")
                .password("hashed")
                .usertype("admin")
                .build();
    }

    @Test
    void login_userNotFound_returns401() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("bob")).thenReturn(Optional.empty());

        var body = new milestone.userservice.dto.LoginRequest();
        body.setUsername("bob");
        body.setPassword("pw");

        ResponseEntity<Map<String, String>> resp = controller.login(body);
        assertEquals(401, resp.getStatusCodeValue());
        assertTrue(resp.getBody().containsKey("error"));
    }

    @Test
    void login_success_returnsToken() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("pw", "hashed")).thenReturn(true);

        Jwt jwt = new Jwt("tkn", Instant.now(), Instant.now().plusSeconds(3600), Map.of("alg","HS256"), Map.of("sub","alice"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        var body = new milestone.userservice.dto.LoginRequest();
        body.setUsername("alice");
        body.setPassword("pw");

        ResponseEntity<Map<String, String>> resp = controller.login(body);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().containsKey("token"));
        assertEquals("tkn", resp.getBody().get("token"));
    }

    @Test
    void login_invalidPassword_returns401() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        var body = new milestone.userservice.dto.LoginRequest();
        body.setUsername("alice");
        body.setPassword("wrong");

        ResponseEntity<Map<String, String>> resp = controller.login(body);
        assertEquals(401, resp.getStatusCodeValue());
    }

    @Test
    void register_conflict_email() {
        User u = User.builder().username("x").email("dup@example.com").password("pw").usertype("user").build();
        when(userRepository.findByEmail("dup@example.com")).thenReturn(Optional.of(u));
        ResponseEntity<?> resp = controller.register(u);
        assertEquals(409, resp.getStatusCodeValue());
    }

    @Test
    void registeringFallback_returnsMessage() {
        ResponseEntity<?> resp = controller.registeringFallback();
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().toString().contains("systems are down"));
    }

//    @Test
//    void register_success_savesUser() {
//        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
//        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("pw")).thenReturn("h");
//
//        User toSave = User.builder().username("newuser").email("new@example.com").password("h").usertype("user").build();
//        User saved = User.builder().id(2).username("newuser").email("new@example.com").password("h").usertype("user").build();
//        when(userRepository.save(any(User.class))).thenReturn(saved);
//
//        ResponseEntity<?> resp = controller.register(toSave);
//        assertEquals(200, resp.getStatusCodeValue());
//        Map<?,?> body = (Map<?,?>)resp.getBody();
//        assertEquals("newuser", body.get("username"));
//    }

    

    @Test
    void getAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));
        ResponseEntity<?> resp = controller.getAllUsers(null);
        assertEquals(200, resp.getStatusCodeValue());
        var list = (java.util.List<?>) resp.getBody();
        assertEquals(1, list.size());
    }
}
