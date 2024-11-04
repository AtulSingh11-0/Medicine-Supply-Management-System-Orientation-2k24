package com.megatronix.medicine.service;

import com.megatronix.medicine.model.TrendingMedicine;
import com.megatronix.medicine.repository.TrendingMedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrendingMedicineService {

	private final TrendingMedicineRepository trendingMedicineRepository;

	// Method to update the trending medicine or create a new one
	public void getTrendingMedicine( LocalDate date, String name, int quantity ) {
		try {
			trendingMedicineRepository.findTrendingMedicineByDateEqualsAndNameEqualsIgnoreCase(date, name)
					.ifPresentOrElse(
							trendingMedicine -> {
								trendingMedicine.setQuantity(trendingMedicine.getQuantity() + quantity);
								trendingMedicineRepository.save(trendingMedicine);
								log.info("Trending medicine updated: {}", trendingMedicine);
							},
							() -> {
								TrendingMedicine trendingMedicine = buildTrendingMedicine(date, name, quantity);
								trendingMedicineRepository.save(trendingMedicine);
								log.info("Trending medicine created: {}", trendingMedicine);
							}
					);
		} catch ( Exception e ) {
			log.error("Error while getting trending medicine: {}", e.getMessage(), e);
			throw new RuntimeException("Error while getting trending medicine: " + e.getMessage(), e);
		}
	}

	// Method to build a new TrendingMedicine object
	private TrendingMedicine buildTrendingMedicine( LocalDate date, String name, int quantity ) {
		return TrendingMedicine.builder()
				.date(date)
				.name(name)
				.quantity(quantity)
				.build();
	}
}
