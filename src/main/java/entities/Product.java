package entities;
import enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name="unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Organization manufacturer;

    @Column(name = "price",nullable = false)
    private int price;

    @Column(name = "manufacture_cost", nullable = false)
    private int manufactureCost;

    @Column(name = "rating", nullable = false)
    private float rating;

    @Column(name = "part_number",length = 67)
    private String partNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // Геттеры и сеттеры
    // Конструктор без параметров
    public Product() {
        this.creationDate = new Date(); // Устанавливается текущая дата и время
    }
}
