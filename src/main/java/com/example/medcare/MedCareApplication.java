package com.example.medcare;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.medcare.embedded.Address;
import com.example.medcare.entities.User;
import com.example.medcare.Enums.Role;
import com.example.medcare.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class MedCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedCareApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Build a supedAdmin
            User superAdmin = User.builder()
            .username("superAdmin")
            .password(passwordEncoder.encode("password"))
            .firstName("John")
            .lastName("Doe")
            .email("")
            .phoneNumber("+1234567890")
            .role(Role.SUPER_ADMIN)
            .createdAt(LocalDate.now())
            .age(25)
            .birthDate(LocalDate.of(1998, 5, 15))
            .address(Address.builder()
            .street("Street Name")
            .city("City")
            .country("Country")
            .build())
            .build();
            userRepository.save(superAdmin);
        };
    }
}