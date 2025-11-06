package milestone.userservice.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserModelTest {

    @Test
    void onCreate_setsCreatedAt() {
        User u = User.builder().username("a").email("b").password("p").usertype("user").build();
        assertNull(u.getCreatedAt());
        u.onCreate();
        assertNotNull(u.getCreatedAt());
    }
}
