package entities;
import enums.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Organization manufacturer;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int manufactureCost;

    @Column(nullable = false)
    private float rating;

    @Column(length = 67)
    private String partNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    // Геттеры и сеттеры
}
