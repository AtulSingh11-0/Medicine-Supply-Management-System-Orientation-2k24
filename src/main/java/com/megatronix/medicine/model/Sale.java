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
@Table(name = "sale")
public class Sale extends Auditable implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "medicine_id", nullable = false)
	private Medicine medicine;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private double totalPrice;

	@Column(nullable = false)
	private LocalDate saleDate;
}
