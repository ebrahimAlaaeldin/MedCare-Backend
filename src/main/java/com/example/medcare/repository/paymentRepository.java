package com.example.medcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.medcare.entities.Payment;

@Repository
public interface paymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findById(int id);
    void deleteById(int id);
}
