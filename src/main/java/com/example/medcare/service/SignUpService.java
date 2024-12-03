package com.example.medcare.service;

import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.SignUpRequest;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.entities.Token;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.repository.TokenRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;


@RequiredArgsConstructor
@Service
public class SignUpService {


    private final PatientRepository patientRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;




    public AuthenticationResponse signUp(SignUpRequest signUpRequest) {

        var stringRole = signUpRequest.getRole().toLowerCase();

        Role role = switch (stringRole) {
            case "doctor" -> Role.DOCTOR;
            case "patient" -> Role.PATIENT;
            default -> throw new IllegalStateException("Unexpected value: " + stringRole);
        };
        AuthenticationResponse response = null;
        if(role == Role.PATIENT) {
            response=patientSignUp(signUpRequest);
        }
        else {
            response=doctorSignUp(signUpRequest);
        }

        return response;


    }

    private AuthenticationResponse patientSignUp(SignUpRequest signUpRequest) {

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
                .createdAt(LocalDate.now().atStartOfDay())
                .build();
        patientRepository.save(patient);

        var jwtToken = jwtService.generateToken(patient);
        tokenRepository.save(Token.builder().token(jwtToken).user(patient).build());
        return AuthenticationResponse.builder().token(jwtToken).build();



    }
    public int calculateAge(LocalDate dateOfBirth) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the period between the current date and the birthdate
        Period period = Period.between(dateOfBirth, currentDate);

        // Return the years part of the period (i.e., the age)
        return period.getYears();
    }

    private AuthenticationResponse doctorSignUp(SignUpRequest signUpRequest) {
        License license = new License(signUpRequest.getLicenseNumber(), signUpRequest.getSpecialty(), signUpRequest.getIssuingDate());

        var doctor = Doctor.builder()
                .username(signUpRequest.getUsername())
                .license(license)
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .role(Role.DOCTOR)
                .phoneNumber(signUpRequest.getPhoneNumber())
                .address(signUpRequest.getAddress())
                .age(signUpRequest.getDateOfBirth().getYear())
                .build();
        doctorRepository.save(doctor);

        var jwtToken = jwtService.generateToken(doctor);
        tokenRepository.save(Token.builder().token(jwtToken).user(doctor).build());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


}
