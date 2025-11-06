package milestone.userservice.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    @Test
    void gettersAndSetters() {
        LoginRequest r = new LoginRequest();
        r.setUsername("u");
        r.setPassword("p");
        assertEquals("u", r.getUsername());
        assertEquals("p", r.getPassword());
    }
}
