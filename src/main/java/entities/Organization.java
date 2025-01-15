package entities;

import enums.OrganizationType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "organization")
public class Organization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address officialAddress;

    @Column(name = "annual_turnover", nullable = false)
    private Float annualTurnover;

    @Column(name = "employees_count", nullable = false)
    private int employeesCount;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "rating",nullable = false)
    private Double rating;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    // Геттеры и сеттеры
}
