package entities;

import enums.OrganizationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address officialAddress;

    @Column(nullable = false)
    private Float annualTurnover;

    @Column(nullable = false)
    private int employeesCount;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Double rating;

    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    // Геттеры и сеттеры
}
