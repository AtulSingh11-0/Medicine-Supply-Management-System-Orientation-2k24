package com.megatronix.medicine.controller;

import com.megatronix.medicine.dto.SellMedicineRequest;
import com.megatronix.medicine.model.ExpiredMedicine;
import com.megatronix.medicine.model.Medicine;
import com.megatronix.medicine.model.Sale;
import com.megatronix.medicine.service.ExpiredMedicineService;
import com.megatronix.medicine.service.MedicineService;
import com.megatronix.medicine.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;
    private final ExpiredMedicineService expiredMedicineService;
    private final SaleService saleService;

    // Endpoint to create a new Medicine
    @PostMapping
    public ResponseEntity<?> createMedicine(
        @RequestBody Medicine medicine
    ) {
        try {
            Medicine createdMedicine = medicineService.createMedicine(medicine);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicine);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating medicine: " + e.getMessage());
        }
    }

    // Endpoint to update an existing Medicine
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicine(
        @PathVariable Integer id, 
        @RequestBody Medicine medicine
    ) {
        try {
            medicine.setId(id); // Set the ID to ensure it is updating the correct medicine
            Medicine updatedMedicine = medicineService.updateMedicine(medicine);
            return ResponseEntity.ok(updatedMedicine);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating medicine: " + e.getMessage());
        }
    }

    // Endpoint to get a Medicine by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicine(
        @PathVariable Integer id
    ) {
        try {
            Medicine medicine = medicineService.getMedicine(id);
            return ResponseEntity.ok(medicine);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving medicine: " + e.getMessage());
        }
    }

    // Endpoint to get all Medicines
    @GetMapping
    public ResponseEntity<?> getAllMedicines() {
        try {
            List<Medicine> medicines = medicineService.getAllMedicines();
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving medicines: " + e.getMessage());
        }
    }

    // Endpoint to get all expired Medicines
    @GetMapping("/expired")
    public ResponseEntity< List< ExpiredMedicine > > getExpiredMedicines() {
        try {
            return ResponseEntity.ok(expiredMedicineService.getAllExpiredMedicines());
        } catch (Exception e) {
            log.error("Error retrieving expired medicines: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    // Endpoint to get Medicines searched by name
    @GetMapping("/search")
    public ResponseEntity< ? > handleSearchMedicineByName (
        @RequestParam(name = "name", required = true, defaultValue = "") String name,
        @RequestParam(name = "quantity", required = false, defaultValue = "0") int quantity
    ) {
        try {
            List< Medicine > searchedMedicineList = medicineService.searchMedicineByName(name, quantity);
            if ( searchedMedicineList.isEmpty() ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicine found with name: " + name);
            }
            return ResponseEntity.ok(searchedMedicineList);
        } catch (Exception e) {
            log.error("Error searching medicine by name: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching medicine by name: " + e.getMessage());
        }
    }

    @PostMapping("/sell")
    public Sale sellMedicine(@RequestBody SellMedicineRequest request) {
        return saleService.sellMedicine(request.getMedicineId(), request.getQuantity());
    }

    @GetMapping("/sold")
    public List< Sale > getAllSoldMedicines() {
        return saleService.getAllSoldMedicines();
    }

}

