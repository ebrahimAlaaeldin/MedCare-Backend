package com.example.medcare.repository;

import com.example.medcare.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {


}
