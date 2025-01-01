package com.example.medcare.repository;

import com.example.medcare.entities.ClinicAdmin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicAdminRepository extends JpaRepository<ClinicAdmin, Integer> {

    Optional<ClinicAdmin> findByUsername(String username);


    Integer findIdByUsername(String username);
    
}
