package com.example.medcare.repository;

import com.example.medcare.entities.Clinic;

import java.util.List;
import java.util.Optional;

import com.example.medcare.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    @Query(value = "SELECT * FROM clinic where is_verified = false", nativeQuery = true)
    List<Clinic> findAllByIsVerifiedFalse();

    Optional<Clinic> findByClinicAdminId(int clinicId);


    Optional<Clinic> findById(int clinicId);

    @Query("SELECT d FROM Clinic c JOIN c.doctors d WHERE c.id = :clinicId")
    List<Doctor> findAllDoctorsByClinicId(@Param("clinicId") int clinicId);











    @Query("SELECT c.clinicId FROM Clinic c WHERE c.clinicAdmin.username = :username")
    Integer findClinicByClinicAdminUsername(@Param("username") String username);


    Optional<Clinic> findByName(String name);
}
