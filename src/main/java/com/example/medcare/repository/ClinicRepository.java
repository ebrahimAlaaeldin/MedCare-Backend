package com.example.medcare.repository;

import com.example.medcare.entities.Clinic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {



    @Query(value = "SELECT * FROM clinic where is_verified = false", nativeQuery = true)
    List<Clinic> findAllByIsVerifiedFalse();


    Optional<Clinic> findById(int clinicId);

    List<Clinic> findByNameContainingIgnoreCase(String name);


}
