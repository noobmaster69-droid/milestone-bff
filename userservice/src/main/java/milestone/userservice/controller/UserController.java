package milestone.userservice.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import milestone.userservice.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import milestone.userservice.model.User;
import milestone.userservice.repository.UserRepository;
import milestone.userservice.dto.UserResponse;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest body) {
        String username = body.getUsername();
        String password = body.getPassword();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            // allow login by email too
            userOpt = userRepository.findByEmail(username);
        }

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Instant now = Instant.now();

        var role = "ROLE_" + user.getUsertype().toUpperCase();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("userservice")
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("authorities", List.of(role))  // <---
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();


        // Explicit HS256 header to match the HMAC encoder configuration
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User req) {
        if (req.getUsername() == null || req.getEmail() == null || req.getPassword() == null || req.getUsertype() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username, email, password and usertype are required"));
        }

        // normalize usertype (helps if downstream queries expect lower-case roles)
        String normalizedType = req.getUsertype().toLowerCase();
        req.setUsertype(normalizedType);

        Optional<User> byEmail = userRepository.findByEmail(req.getEmail());
        if (byEmail.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "email already in use"));
        }

        Optional<User> byUsername = userRepository.findByUsername(req.getUsername());
        if (byUsername.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "username already in use"));
        }

        String hashed = passwordEncoder.encode(req.getPassword());
        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(hashed)
                .usertype(normalizedType)
                .build();

        User saved = userRepository.save(u);
        return ResponseEntity.ok(Map.of(
                "id", String.valueOf(saved.getId()),
                "username", saved.getUsername(),
                "email", saved.getEmail()
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        List<User> users = userRepository.findAll();
        List<UserResponse> resp = users.stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getUsertype(), u.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }
}
