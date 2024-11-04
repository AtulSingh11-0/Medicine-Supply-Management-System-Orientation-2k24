package com.megatronix.medicine.repository;

import com.megatronix.medicine.model.TrendingMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TrendingMedicineRepository extends JpaRepository< TrendingMedicine, Long> {
	public Optional< TrendingMedicine > findTrendingMedicineByDateEqualsAndNameEqualsIgnoreCase ( LocalDate date, String name );
}
