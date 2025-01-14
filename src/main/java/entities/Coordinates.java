package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "coordinates")
public class Coordinates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="x", nullable = false)
    private double x;

    @Column(name="y", nullable = false)
    private long y;

    // Геттеры и сеттеры
}
