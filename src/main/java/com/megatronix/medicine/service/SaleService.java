package com.megatronix.medicine.service;

import com.megatronix.medicine.model.Medicine;
import com.megatronix.medicine.model.Sale;
import com.megatronix.medicine.repository.MedicineRepository;
import com.megatronix.medicine.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService {
	private final MedicineRepository medicineRepository;
	private final SaleRepository saleRepository;

	@Transactional
	public Sale sellMedicine(Integer medicineId, int saleQuantity) {
		Medicine medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new IllegalArgumentException("Medicine not found"));

		medicine.deductQuantity(saleQuantity);
		medicineRepository.save(medicine); // Update quantity in the Medicine table

		double totalPrice = saleQuantity * ( medicine.getPrice() - (medicine.getPrice() * medicine.getDiscount() / 100) );
		Sale sale = new Sale();
		sale.setMedicine(medicine);
		sale.setQuantity(saleQuantity);
		sale.setTotalPrice(totalPrice);
		sale.setSaleDate(LocalDate.now());

		return saleRepository.save(sale);
	}

	public List< Sale > getAllSoldMedicines() {
		return saleRepository.findAll();
	}
}
