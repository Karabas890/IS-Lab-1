package entities;
import enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="username", unique = true)
    private String username;
    @NotNull
    @Column(name="password")
    @Size(min=2)
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="request_admin_rights", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean requestAdminRights;

    // Конструкторы
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public boolean getRequestAdminRights() {
        return requestAdminRights;
    }

    public void setRequestAdminRights(Boolean requestAdminRights) {
        this.requestAdminRights = requestAdminRights;
    }
}
