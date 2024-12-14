package com.example.medcare.service;

import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;

import com.example.medcare.dto.ResponseDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;

import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
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



    public ResponseEntity<Object> patientSignUp(PatientDTO signUpRequest) {

        // Validate required fields
        if (signUpValidation(signUpRequest) != null) {
            return signUpValidation(signUpRequest);
        }
        try {
            String jwtToken = savePatient(signUpRequest);
            return ResponseEntity.ok().body(ResponseDTO.builder().message("Patient registered successfully").statusCode(200).data(jwtToken).build());

        }catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintMessage(e.getMessage());

            return ResponseEntity.badRequest().body(ResponseDTO.builder().message(errorMessage).statusCode(400).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseDTO.builder().message("An unexpected error occurred").statusCode(500).build());
        }

    }

    public ResponseEntity<Object> doctorSignUp(DoctorDTO signUpRequest) {
        // Validate required fields
        if (signUpValidation(signUpRequest) != null) {
            return signUpValidation(signUpRequest);
        }
        License license = new License(signUpRequest.getLicenseNumber(), signUpRequest.getSpecialty(),
                signUpRequest.getIssuingDate());
        try {
        String jwtToken = saveDoctor(signUpRequest, license);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("message", "Doctor registered successfully");
        return ResponseEntity.ok().headers(responseHeader).body(
                ResponseDTO.builder().message("Doctor registered successfully").statusCode(200).data(
                        jwtToken).build());
        }
        catch (DataIntegrityViolationException e) {
            String errorMessage = extractConstraintMessage(e.getMessage());
            return ResponseEntity.badRequest().body(ResponseDTO.builder().message(errorMessage).statusCode(400).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseDTO.builder().message("An unexpected error occurred").statusCode(500).build());
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

//      return full data integrity violation message
//    private String extractConstraintMessage(String fullMessage) {
//        int startIndex = fullMessage.indexOf("Key (");
//        int endIndex = fullMessage.indexOf(") already exists");
//        if (startIndex != -1 && endIndex != -1) {
//            // Extract the substring including the field name and value
//            String keyValuePair = fullMessage.substring(startIndex + "Key (".length(), endIndex);
//            // Remove the parentheses surrounding the field name
//            return keyValuePair.replace("(", "").replace(")", "") + " already exists";
//        }
//        return "Duplicate key error";
//    }

    // return a more user-friendly error message
    private String extractConstraintMessage(String fullMessage) {
        int startIndex = fullMessage.indexOf("Key (");
        int endIndex = fullMessage.indexOf("already exists");
        if (startIndex != -1 && endIndex != -1) {
            // Extract the field name (e.g., email, username)

            String keyValuePair = fullMessage.substring(startIndex + "Key (".length(), endIndex);
            String fieldName = keyValuePair.split("=")[0].trim(); // Extract only the field name
            fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1); // Remove the package name
            //remove ) from the end of the field name
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

    private ResponseEntity<Object> signUpValidation(Object signUpRequest) {
        if (signUpRequest instanceof PatientDTO) {
            PatientDTO patientDTO = (PatientDTO) signUpRequest;
            if(patientDTO.getEmail() ==null||
                    patientDTO.getFirstName() == null ||
                    patientDTO.getLastName() == null ||
                    patientDTO.getPassword() == null ||
                    patientDTO.getUsername() == null ||
                    patientDTO.getInsuranceNumber() == null ||
                    patientDTO.getEmergencyContactNumber() == null ||
                    patientDTO.getPhoneNumber() == null ||
                    patientDTO.getAddress() == null ||
                    patientDTO.getDateOfBirth() == null) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("Missing required fields").statusCode(400).data(null).build());
            }

        }
        else if (signUpRequest instanceof DoctorDTO) {
            DoctorDTO doctorDTO = (DoctorDTO) signUpRequest;
            if (doctorDTO.getEmail() == null ||
                    doctorDTO.getFirstName() == null ||
                    doctorDTO.getLastName() == null ||
                    doctorDTO.getPassword() == null ||
                    doctorDTO.getUsername() == null ||
                    doctorDTO.getLicenseNumber() == null ||
                    doctorDTO.getSpecialty() == null ||
                    doctorDTO.getIssuingDate() == null ||
                    doctorDTO.getPhoneNumber() == null ||
                    doctorDTO.getAddress() == null ||
                    doctorDTO.getDateOfBirth() == null) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("Missing required fields").statusCode(400).data(null).build());
            }
        }
        return null;
    }
}
