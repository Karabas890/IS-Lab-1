package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "location")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="x", nullable = false)
    private Integer x;

    @Column(name="y", nullable = false)
    private Integer y;

    @Column(name="z")
    private float z;

    @Column(name="name", nullable = false)
    private String name;

    // Геттеры и сеттеры
}
