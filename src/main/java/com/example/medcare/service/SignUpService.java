package com.example.medcare.service;

import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.ClinicAdmin;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final PatientRepository patientRepository;
    private final JwtService jwtService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicAdminRepository clinicAdminRepository;

    public ResponseEntity<Object> patientSignUp(PatientDTO signUpRequest) {

        try {
            String jwtToken = savePatient(signUpRequest);
            return ResponseEntity.ok().body(ResponseMessageDto.builder().success(true)
                    .message("Patient registered successfully").statusCode(200).data(jwtToken).build());

        } catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseMessageDto.builder().success(false).message(errorMessage).statusCode(400).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseMessageDto.builder().success(false)
                    .message("An unexpected error occurred").statusCode(500).build());
        }

    }

    public ResponseEntity<Object> doctorSignUp(DoctorDTO signUpRequest) {

        License license = new License(signUpRequest.getLicenseNumber(), signUpRequest.getSpecialty(),
                signUpRequest.getIssuingDate());
        try {
            String jwtToken = saveDoctor(signUpRequest, license);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("message", "Doctor registered successfully");
            return ResponseEntity.ok().headers(responseHeader).body(
                    ResponseMessageDto.builder().success(true).message("Doctor registered successfully").statusCode(200)
                            .data(jwtToken).build());
        } catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseMessageDto.builder().success(false).message(errorMessage).statusCode(400).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseMessageDto.builder().success(false)
                    .message("An unexpected error occurred").statusCode(500).build());
        }
    }

    @Transactional
    public String saveDoctor(DoctorDTO signUpRequest, License license) {

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
                .age(calculateAge(signUpRequest.getDateOfBirth()))
                .birthDate(signUpRequest.getDateOfBirth())
                .createdAt(LocalDate.now())
                .gender(signUpRequest.getGender())
                .build();
        doctorRepository.save(doctor);
        Map<String, Object> extraClaims = Map.of(
                "role", doctor.getRole().name(),
                "firstName", doctor.getFirstName(),
                "lastName", doctor.getLastName(),
                "username", doctor.getUsername(),
                "email", doctor.getEmail());
        return jwtService.generateToken(extraClaims, doctor);
    }

    @Transactional
    public String savePatient(PatientDTO signUpRequest) {
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
                .gender(signUpRequest.getGender())
                .build();
        patientRepository.save(patient);
        Map<String, Object> extraClaims = Map.of(
                "role", patient.getRole().name(),
                "firstName", patient.getFirstName(),
                "lastName", patient.getLastName(),
                "username", patient.getUsername(),
                "email", patient.getEmail());

        return jwtService.generateToken(extraClaims, patient);

    }

    // return a more user-friendly error message
    private String extractConstraintMessage(String fullMessage) {
        int startIndex = fullMessage.indexOf("Key (");
        int endIndex = fullMessage.indexOf("already exists");
        if (startIndex != -1 && endIndex != -1) {
            // Extract the field name (e.g., email, username)

            String keyValuePair = fullMessage.substring(startIndex + "Key (".length(), endIndex);
            String fieldName = keyValuePair.split("=")[0].trim(); // Extract only the field name
            fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1); // Remove the package name
            // remove ) from the end of the field name
            fieldName = fieldName.substring(0, fieldName.length() - 1);
            return fieldName + " already exists"; // Simplify the message
        }
        return "Duplicate key error";
    }

    public int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears();

    }

    public ResponseEntity<Object> adminSignUp(ClinicAdminDTO signUpRequest) {

        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()
                || signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty())

        {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDto.builder()
                            .message("Invalid sign up request")
                            .success(false)
                            .statusCode(400)
                            .build());
        }

            ClinicAdmin admin = ClinicAdmin.builder()
                    .username(signUpRequest.getUsername())
                    .firstName(signUpRequest.getFirstName())
                    .lastName(signUpRequest.getLastName())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .email(signUpRequest.getEmail())
                    .role(Role.ADMIN)
                    .phoneNumber(signUpRequest.getPhoneNumber())
                    .address(
                            Address.builder()
                                    .city(signUpRequest.getAddress().getCity())
                                    .country(signUpRequest.getAddress().getCountry())
                                    .street(signUpRequest.getAddress().getStreet())
                                    .zipCode(signUpRequest.getAddress().getZipCode())
                                    .build())
                    .clinic(Clinic.builder().name(signUpRequest.getClinicName()).build())
                    .age(calculateAge(signUpRequest.getDateOfBirth()))
                    .birthDate(signUpRequest.getDateOfBirth())
                    .createdAt(LocalDate.now())
                    .build();

            clinicAdminRepository.save(admin);

            Map<String, Object> extraClaims = Map.of(
                    "role", admin.getRole().name(),
                    "firstName", admin.getFirstName(),
                    "lastName", admin.getLastName(),
                    "username", admin.getUsername(),
                    "email", admin.getEmail());

            var jwtToken = jwtService.generateToken(extraClaims, admin);
            return ResponseEntity.ok(
                    ResponseMessageDto.builder()
                            .message("Admin registered successfully, waiting for verification")
                            .success(true)
                            .statusCode(200)
                            .data(jwtToken)
                            .build());
        }
    }
