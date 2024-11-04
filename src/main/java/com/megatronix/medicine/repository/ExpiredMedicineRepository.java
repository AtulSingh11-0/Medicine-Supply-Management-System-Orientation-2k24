package com.megatronix.medicine.repository;

import com.megatronix.medicine.model.ExpiredMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredMedicineRepository extends JpaRepository< ExpiredMedicine, Integer> {
}
