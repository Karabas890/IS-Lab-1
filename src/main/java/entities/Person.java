package entities;
import enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "person")
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1) // Строка не может быть пустой
    @Column(nullable = false)
    private String name;

    @Column(name="eye_color")
    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(name="hair_color")
    private Color hairColor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NotNull
    @Positive // Значение должно быть больше 0
    @Column(nullable = false)
    private float height;

    @Positive // Значение должно быть больше 0, но поле может быть null
    @Column
    private Float weight;

    @NotNull
    @Size(min = 6, max = 33) // Длина строки должна быть от 6 до 33
    @Column(name="passport_id", nullable = false, unique = true, length = 33)
    private String passportID;
    // Геттеры и сеттеры
}