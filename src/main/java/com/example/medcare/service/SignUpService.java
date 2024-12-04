package com.example.medcare.service;

import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;

import com.example.medcare.embedded.License;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;

import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final PatientRepository patientRepository;
    private final JwtService jwtService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseMessageDto patientSignUp(PatientDTO signUpRequest) {
                // Validate required fields
                if (signUpRequest.getUsername() == null || signUpRequest.getUsername().isEmpty() ||
                                signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty() ||
                                signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
                        return ResponseMessageDto.builder()
                                        .message("Invalid sign up request")
                                        .success(false)
                                        .statusCode(400)
                                        .build();
                }
            


        var patient = Patient.builder()
                .username(signUpRequest.getUsername())
                .insuranceId(signUpRequest.getInsuranceNumber())
                .emergencyContactPhone(signUpRequest.getEmergencyContactNumber())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .role(Role.PATIENT)
                .phoneNumber(signUpRequest.getPhoneNumber())
                .address(signUpRequest.getAddress())
                .age(calculateAge(signUpRequest.getDateOfBirth()))
                .birthDate(signUpRequest.getDateOfBirth())
                .createdAt(LocalDate.now())
                .build();
        patientRepository.save(patient);

        Map<String, Object> extraClaims = Map.of(
                "role", patient.getRole().name(),
                "firstName", patient.getFirstName(),
                "lastName", patient.getLastName(),
                "username", patient.getUsername(),
                "email", patient.getEmail());

       

        var jwtToken = jwtService.generateToken(extraClaims, patient);

        return ResponseMessageDto.builder()
                .message("Patient registered successfully")
                .success(true)
                .statusCode(200)
                .data(jwtToken)
                .build();
    }

    public int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears();
    }

    public ResponseMessageDto doctorSignUp(DoctorDTO signUpRequest) {
        // Validate required fields
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().isEmpty() ||
                signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty() ||
                signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
            return ResponseMessageDto.builder()
                    .message("Invalid sign up request")
                    .success(false)
                    .statusCode(400)
                    .build();
        }

        // Create the License object
        License license = new License(signUpRequest.getLicenseNumber(), signUpRequest.getSpecialty(),
                signUpRequest.getIssuingDate());

        // Build the Doctor entity
        var doctor = Doctor.builder()
                .isVerified(false)
                .username(signUpRequest.getUsername())
                .license(license)
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .role(Role.DOCTOR)
                .birthDate(signUpRequest.getDateOfBirth())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .address(signUpRequest.getAddress())
                .age(calculateAge(signUpRequest.getDateOfBirth()))
                .createdAt(LocalDate.now())
                .build();

        doctorRepository.save(doctor);

        // Prepare extra claims for JWT
        Map<String, Object> extraClaims = Map.of(
                "role", doctor.getRole().name(),
                "firstName", doctor.getFirstName(),
                "lastName", doctor.getLastName(),
                "username", doctor.getUsername(),
                "email", doctor.getEmail());

        var jwtToken = jwtService.generateToken(extraClaims, doctor);

        return ResponseMessageDto.builder()
                .message("Doctor registered successfully, Waiting for License verification")
                .success(true)
                .statusCode(200)
                .data(jwtToken)
                .build();
    }
}
