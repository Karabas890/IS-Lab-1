package entities;

import enums.OrganizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "organization")
public class Organization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank // Поле не может быть null и не может быть пустой строкой
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address officialAddress;

    @NotNull // Поле не может быть null
    @Positive // Значение должно быть больше 0
    @Column(name = "annual_turnover", nullable = false)
    private Float annualTurnover;

    @Positive // Значение должно быть больше 0
    @NotNull
    @Column(name = "employees_count", nullable = false)
    private int employeesCount;

    @NotBlank // Поле не может быть null и не может быть пустой строкой
    @NotNull
    @Column(name = "full_name",nullable = false)
    private String fullName;

    @NotNull // Поле не может быть null
    @Positive // Значение должно быть больше 0
    @Column(name = "rating",nullable = false)
    private Double rating;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    // Геттеры и сеттеры
}
