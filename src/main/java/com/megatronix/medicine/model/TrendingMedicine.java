package com.megatronix.medicine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "trending_medicine")
public class TrendingMedicine extends Auditable implements Serializable {
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY )
	private Integer id;

	@Column ( nullable = false )
	private String name;

	@Column ( nullable = false )
	private int quantity;

	@JsonFormat (
			pattern = "yyyy-MM-dd",
			timezone = "Asia/Kolkata",
			shape = JsonFormat.Shape.STRING,
			locale = "en_IN"
	)
	private LocalDate date;
}
