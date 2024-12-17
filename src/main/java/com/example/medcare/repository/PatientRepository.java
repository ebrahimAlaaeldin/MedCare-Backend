package com.example.medcare.repository;

import com.example.medcare.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUsername(String username);
    Optional<Patient> findById(Integer id);
}
