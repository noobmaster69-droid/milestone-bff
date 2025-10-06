package milestone.userservice.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String usertype;
    private LocalDateTime createdAt;

    public UserResponse(Integer id, String username, String email, String usertype, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.usertype = usertype;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getUsertype() { return usertype; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
