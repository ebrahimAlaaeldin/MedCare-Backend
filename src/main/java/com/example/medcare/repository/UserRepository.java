package com.example.medcare.repository;


import com.example.medcare.entities.Patient;
import com.example.medcare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPinNumber(String pinNumber);
    @Query("SELECT u FROM Patient u")
    List<Patient> findAllPatients();

}
