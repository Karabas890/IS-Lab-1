package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Setter
@Getter
@Table(name = "ImportHistory")
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name="status")
    private String status;
    @Column(name="count_added")
    private Integer countAdded;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    public ImportHistory(){}
    public ImportHistory(String status, String username, Integer countAdded){
        this.status = status;
        this.username = username;
        this.countAdded = countAdded;
    }
    @Override
    public String toString() {
        return "ImportHistory{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                ", countAdded=" + countAdded +
                ", createdAt=" + createdAt +
                '}';
    }


}
