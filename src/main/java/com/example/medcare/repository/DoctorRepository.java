package com.example.medcare.repository;

import com.example.medcare.entities.Doctor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findAllByIsVerified(Boolean isVerified);

    Optional<Doctor> findByUsername(String username);

    List<Doctor> findBySpecializationAndClinicId(String specialization, int clinicId);
    
}
