package com.megatronix.medicine.service;

import com.megatronix.medicine.model.Medicine;
import com.megatronix.medicine.repository.MedicineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository repository;
    private final TrendingMedicineService trendingMedicineService;

    // Method to create a new Medicine
    public Medicine createMedicine(Medicine medicine) {
        try {
            Medicine saved = repository.save(medicine);
            log.info("Medicine created: {}", saved);
            return saved;
        } catch (Exception e) {
            log.error("Failed to create the medicine: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create the medicine: " + e.getMessage(), e);
        }
    }

    // Method to update an existing Medicine
    public Medicine updateMedicine(Medicine medicine) {
        try {
            Optional<Medicine> existingMedicine = repository.findById(medicine.getId());
            log.info("Medicine found: {}", existingMedicine);

            if (existingMedicine.isPresent()) {
                // Update only fields that are not null in the incoming medicine object
                Medicine updatedMedicine = existingMedicine.get();
                updatedMedicine.setName(Optional.ofNullable(medicine.getName()).orElse(updatedMedicine.getName()));
                updatedMedicine.setManufacturer(Optional.ofNullable(medicine.getManufacturer()).orElse(updatedMedicine.getManufacturer()));
                updatedMedicine.setGenericName(Optional.ofNullable(medicine.getGenericName()).orElse(updatedMedicine.getGenericName()));
                updatedMedicine.setDosage(Optional.ofNullable(medicine.getDosage()).orElse(updatedMedicine.getDosage()));
                updatedMedicine.setQuantity(medicine.getQuantity() != 0 ? medicine.getQuantity() : updatedMedicine.getQuantity());
                updatedMedicine.setPrice(medicine.getPrice() != 0 ? medicine.getPrice() : updatedMedicine.getPrice());
                updatedMedicine.setDiscount(medicine.getDiscount() != 0 ? medicine.getDiscount() : updatedMedicine.getDiscount());
                updatedMedicine.setExpiryDate(Optional.ofNullable(medicine.getExpiryDate()).orElse(updatedMedicine.getExpiryDate()));
                log.info("Medicine updated: {}", updatedMedicine);

                return repository.save(updatedMedicine);
            } else {
                log.error("Medicine with ID {} not found.", medicine.getId());
                throw new EntityNotFoundException("Medicine with ID " + medicine.getId() + " not found.");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Failed to update medicine: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the medicine: " + e.getMessage(), e);
        }
    }

    // Method to get a Medicine by ID
    public Medicine getMedicine(Integer id) {
        try {
            log.info("Retrieving medicine with ID: {}", id);

            Optional<Medicine> medicine = repository.findById(id);
            log.info("Medicine found: {}", medicine);

            return medicine.orElseThrow(() -> new EntityNotFoundException("Medicine with ID " + id + " not found."));
        } catch (EntityNotFoundException e) {
            log.error("Medicine with ID {} not found.", id);
            throw new RuntimeException("Failed to retrieve medicine: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving the medicine: " + e.getMessage(), e);
        }
    }

    // Method to get all Medicines
    public List<Medicine> getAllMedicines() {
        try {
            List< Medicine > all = repository.findAll();
            log.info("Retrieved all medicines: {}", all.size());
            return all;
        } catch (Exception e) {
            log.error("Failed to retrieve medicines: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve medicines: " + e.getMessage(), e);
        }
    }

    // Method to search for Medicines by name
    public List<Medicine> searchMedicineByName (String name, int quantity) {
        try {
            // call the trending medicine service to update or create the trending medicine
            trendingMedicineService.getTrendingMedicine(LocalDate.now(), name, quantity == 0 ? 1 : quantity);

            // search the medicine by name
            List< Medicine > searchedMedicineList = repository.findAllByNameContainingIgnoreCase(name);
            // if no medicine found, return empty list
            if ( searchedMedicineList.isEmpty() ) {
                log.error("No medicine found with name: {}", name);
                return new ArrayList<>();
            }
            // return the list of medicines found
            log.info("List of medicines found with name: {}", searchedMedicineList.size());
            return searchedMedicineList;
        } catch ( RuntimeException e ) {
            log.error("Failed to search medicine by name: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search medicine by name: " + e.getMessage(), e);
        }
    }
}