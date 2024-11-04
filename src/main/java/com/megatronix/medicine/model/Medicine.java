package com.megatronix.medicine.model;

import com.megatronix.medicine.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicine")
public class Medicine extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String manufacturer;

    private String genericName;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    private float discount;

    @Column(nullable = false)
    private LocalDate expiryDate;

    public void deductQuantity(int saleQuantity) {
        if (this.quantity >= saleQuantity) {
            this.quantity -= saleQuantity;
        } else {
            throw new IllegalArgumentException("Insufficient quantity in stock");
        }
    }

}