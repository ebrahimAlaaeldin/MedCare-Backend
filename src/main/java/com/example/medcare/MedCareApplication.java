package com.example.medcare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.medcare.entities.User;
import com.example.medcare.enums.Role;
import com.example.medcare.repository.UserRepository;

@SpringBootApplication
public class MedCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedCareApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            // Check if the SuperAdmin already exists to avoid duplicates
            if (userRepository.findByUsername("superadmin") == null) {
                // Hash the password using BCrypt
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode("supersecret");

                // Create a new SuperAdmin user
                User superAdmin = new User();
                superAdmin.setUsername("superadmin");
                superAdmin.setPassword(hashedPassword); // Set the hashed password
                superAdmin.setEmail("superadmin@example.com");
                superAdmin.setRole(Role.SUPER_ADMIN);
                superAdmin.setFirstName("Super");
                superAdmin.setLastName("Admin");
               
                superAdmin.setPhoneNumber("0123456789");
                
                superAdmin.setAge(5);
                superAdmin.setCreatedAt(java.time.LocalDateTime.now());
                

                // Save the SuperAdmin user to the database
                userRepository.save(superAdmin);

                System.out.println("SuperAdmin seeded successfully!");
            }
        };

    }
}