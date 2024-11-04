package com.megatronix.medicine.repository;

import com.megatronix.medicine.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
	List < Medicine > findAllByExpiryDateBefore ( LocalDate date );
	List < Medicine > findAllByNameContainingIgnoreCase ( String name );
}
