package entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "address")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name="zip_code", nullable = false)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location town;

    // Геттеры и сеттеры
}
