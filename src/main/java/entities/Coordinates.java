package entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private long y;

    // Геттеры и сеттеры
}
