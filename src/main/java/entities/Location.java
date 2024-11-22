package entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @Column
    private float z;

    @Column(nullable = false)
    private String name;

    // Геттеры и сеттеры
}
