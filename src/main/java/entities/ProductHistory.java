package entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "product_history")
public class ProductHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "operation_type", nullable = false)
    private String operationType; // Например: "CREATE", "UPDATE", "DELETE"

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "operation_date", nullable = false)
    private Date operationDate;

    @Column(name = "comment")
    private String comment;

    // Геттеры и сеттеры
}

