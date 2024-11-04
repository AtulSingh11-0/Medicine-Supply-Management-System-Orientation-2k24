package com.megatronix.medicine.repository;

import com.megatronix.medicine.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository< Sale, Long> {
}
