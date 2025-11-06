package milestone.userservice.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class UserResponseTest {

    @Test
    void constructor_and_getters() {
        LocalDateTime now = LocalDateTime.now();
        UserResponse r = new UserResponse(1, "u", "e@x.com", "user", now);
        assertEquals(1, r.getId());
        assertEquals("u", r.getUsername());
        assertEquals("e@x.com", r.getEmail());
        assertEquals("user", r.getUsertype());
        assertEquals(now, r.getCreatedAt());
    }
}
