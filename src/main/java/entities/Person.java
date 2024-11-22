package entities;
import enums.*;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private float height;

    @Column
    private Float weight;

    @Column(nullable = false, unique = true, length = 33)
    private String passportID;

    // Геттеры и сеттеры
}
