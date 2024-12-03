package com.example.medcare.repository;

import com.example.medcare.entities.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

}
