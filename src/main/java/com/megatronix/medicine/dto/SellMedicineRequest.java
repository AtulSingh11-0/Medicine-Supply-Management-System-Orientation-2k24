package com.megatronix.medicine.dto;

import lombok.Data;

@Data
public class SellMedicineRequest {
	private Integer medicineId;
	private int quantity;
}
