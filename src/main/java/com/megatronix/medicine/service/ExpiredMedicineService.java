package com.megatronix.medicine.service;

import com.megatronix.medicine.model.ExpiredMedicine;
import com.megatronix.medicine.model.Medicine;
import com.megatronix.medicine.repository.ExpiredMedicineRepository;
import com.megatronix.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiredMedicineService {

	private final MedicineRepository medicineRepository;
	private final ExpiredMedicineRepository expiredMedicineRepository;

	@Scheduled(cron = "0 0 0 * * *") // Run every day at 12:00 AM
	public void processExpiredMedicines( ) {
		log.info("Starting expired medicines processing at 12AM on {}", LocalDate.now());
		try {
			LocalDate date = LocalDate.now();

			List< Medicine > expiredMedicines = medicineRepository.findAllByExpiryDateBefore(date);

			if ( expiredMedicines.isEmpty() ) {
				log.info("No expired medicines found on {}", date);
				return;
			}

			List< ExpiredMedicine > expiredMedicineList = expiredMedicines.stream()
					.map(this::build)
					.toList();

			expiredMedicineRepository.saveAll(expiredMedicineList);
			medicineRepository.deleteAll(expiredMedicines);

			log.info("Successfully moved {} expired medicines to expired_medicines table", expiredMedicines.size());

			log.info("Expired medicines processing completed at 12AM on {}", LocalDate.now());
		} catch ( Exception e ) {
			log.error("Error in getting all expired medicines: {}", e.getMessage(), e);
		}
	}

	public List< ExpiredMedicine > getAllExpiredMedicines () {
		try {
			List< ExpiredMedicine > all = expiredMedicineRepository.findAll();
			log.info("Successfully fetched all expired medicines: {}", all.size());
			return all;
		} catch ( Exception e ) {
			log.error("Error in getting all expired medicines: {}", e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	private ExpiredMedicine build ( Medicine expiredMedicine ) {
		return ExpiredMedicine.builder()
				.name(expiredMedicine.getName())
				.price(expiredMedicine.getPrice())
				.manufacturer(expiredMedicine.getManufacturer())
				.genericName(expiredMedicine.getGenericName())
				.dosage(expiredMedicine.getDosage())
				.quantity(expiredMedicine.getQuantity())
				.discount(expiredMedicine.getDiscount())
				.build();
	}

}
