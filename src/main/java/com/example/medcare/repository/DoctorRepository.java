package com.example.medcare.repository;

import com.example.medcare.entities.Doctor;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findAllByIsVerified(Boolean isVerified);


//    Optional<Doctor> findByUsername(String username);

    @Query("SELECT d FROM Doctor d join User u  on u.id = d.id WHERE u.username = :username")
    Optional<Doctor> findByUsername(@Param("username") String username);








}
