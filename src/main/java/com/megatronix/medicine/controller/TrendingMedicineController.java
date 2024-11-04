package com.megatronix.medicine.controller;

import com.megatronix.medicine.model.TrendingMedicine;
import com.megatronix.medicine.repository.TrendingMedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping ("/api/trending-medicines")
@RequiredArgsConstructor
public class TrendingMedicineController {
	private final TrendingMedicineRepository repository;

	@GetMapping
	public ResponseEntity< ? > handleGetTrendingMedicines() {
		try {
			List< TrendingMedicine > trendingMedicines = repository.findAll();
			return ResponseEntity.ok( trendingMedicines );
		} catch ( Exception e ) {
			return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
					.body( "Error getting trending medicines: " + e.getMessage() );
		}
	}
}
