package entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "coordinates")
public class Coordinates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Max(648)
    @NotNull
    @Column(name="x", nullable = false)
    private double x;

    @NotNull
    @Column(name="y", nullable = false)
    private long y;

    // Геттеры и сеттеры
}
