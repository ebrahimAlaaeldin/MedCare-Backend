package com.example.medcare.repository;

import com.example.medcare.entities.ForgotPassword;
import com.example.medcare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {



    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    Optional<ForgotPassword> findByUser(User user);
    boolean existsByUser(User user);

    boolean existsByOtp(int randomOtp);
}
