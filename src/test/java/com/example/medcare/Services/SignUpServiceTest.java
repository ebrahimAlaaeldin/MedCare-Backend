package com.example.medcare.Services;

import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.entities.ClinicAdmin;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SignUpServiceTest {

    @InjectMocks
    @Spy
    private SignUpService signUpService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicAdminRepository clinicAdminRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // Properly mocked

    @Mock
    private JwtService jwtService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void signUp_WhenPatientAlreadyExists_ShouldReturnConflict() {
        // Arrange
        PatientDTO patientDTO = PatientDTO.builder()
                .username("john_doe")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(Role.PATIENT)
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();

        // Simulate exception during save
        doThrow(new DataIntegrityViolationException("User already exists"))
                .when(patientRepository).save(any());
        // Mock password encoding
        when(passwordEncoder.encode(patientDTO.getPassword())).thenReturn("encodedPassword123");

        when((signUpService).extractConstraintMessage("User already exists"))
                .thenReturn("User already exists");


        // Act
        ResponseEntity<Object> response = signUpService.patientSignUp(patientDTO);
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        String expectedMessage = responseMessageDto.getMessage();

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCodeValue());
        assertEquals("User already exists", responseMessageDto.getMessage());
        assertFalse(responseMessageDto.isSuccess());
    }

    @Test
    public void signUpDoctor_WhenDoctorAlreadyExists_ShouldReturnConflict() {
        // Arrange
        DoctorDTO doctorDTO = DoctorDTO.builder()
                .username("john_doe")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .email("test.example.com")
                .phoneNumber("1234567890")
                .issuingDate(LocalDate.now().minusYears(5))
                .licenseNumber("1234567890")
                .Specialty("Cardiology")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();

        // Simulate exception during save
        doThrow(new DataIntegrityViolationException("User already exists"))
                .when(doctorRepository).save(any());
        // Mock password encoding
        when(passwordEncoder.encode(doctorDTO.getPassword())).thenReturn("encodedPassword123");

        when((signUpService).extractConstraintMessage("User already exists"))
                .thenReturn("User already exists");

        // Act
        ResponseEntity<Object> response = signUpService.doctorSignUp(doctorDTO);
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        String expectedMessage = responseMessageDto.getMessage();

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCodeValue());
        assertEquals("User already exists", responseMessageDto.getMessage());
        assertFalse(responseMessageDto.isSuccess());
    }

    @Test
    public void signUp_withValidPatientDTO_ShouldReturnSuccess() {
        // Arrange
        PatientDTO patientDTO = PatientDTO.builder()
                .username("john_doe")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(Role.PATIENT)
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();
        // Create a mock Patient object to simulate repository save result
        Patient savedPatient = new Patient();
        savedPatient.setUsername(patientDTO.getUsername());
        savedPatient.setPassword("encodedPassword123");
        savedPatient.setFirstName(patientDTO.getFirstName());
        savedPatient.setLastName(patientDTO.getLastName());
        savedPatient.setEmail(patientDTO.getEmail());
        savedPatient.setRole(Role.PATIENT);
        // Mock password encoding
        when(passwordEncoder.encode(patientDTO.getPassword())).thenReturn("encodedPassword123");
        when(patientRepository.save(any())).thenReturn(savedPatient);
        when(jwtService.generateToken(anyMap(),any())).thenReturn("token");

        // Act
        ResponseEntity<Object> response = signUpService.patientSignUp(patientDTO);
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        String expectedMessage = responseMessageDto.getMessage();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Patient registered successfully", responseMessageDto.getMessage());
        assertTrue(responseMessageDto.isSuccess());

    }

    @Test
    public void signUp_withValidDoctorDTO_ShouldReturnSuccess() {
        // Arrange
        DoctorDTO doctorDTO = DoctorDTO.builder()
                .username("john_doe")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .email("test.example.com")
                .phoneNumber("1234567890")
                .issuingDate(LocalDate.now().minusYears(5))
                .licenseNumber("1234567890")
                .Specialty("Cardiology")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();
        // Create a mock Doctor object to simulate repository save result
        Doctor savedDoctor = new Doctor();
        savedDoctor.setUsername(doctorDTO.getUsername());
        savedDoctor.setPassword("encodedPassword123");
        savedDoctor.setFirstName(doctorDTO.getFirstName());
        savedDoctor.setLastName(doctorDTO.getLastName());
        savedDoctor.setEmail(doctorDTO.getEmail());
        // Mock password encoding
        when(passwordEncoder.encode(doctorDTO.getPassword())).thenReturn("encodedPassword123");
        when(doctorRepository.save(any())).thenReturn(savedDoctor);
        when(jwtService.generateToken(anyMap(),any())).thenReturn("token");

        // Act
        ResponseEntity<Object> response = signUpService.doctorSignUp(doctorDTO);
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        String expectedMessage = responseMessageDto.getMessage();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Doctor registered successfully", responseMessageDto.getMessage());
        assertTrue(responseMessageDto.isSuccess());
    }

    @Test
    public void calculateAge_WhenDateOfBirthIsGiven_ShouldReturnAge() {
        // Arrange
        LocalDate dateOfBirth = LocalDate.of(1999, 12, 31);
        // Act
        Integer age = signUpService.calculateAge(dateOfBirth);
        // Assert
        assertEquals(24, age);
    }

   @Test
    public void adminSignUp_withoutPassword() {
       // Arrange
       ClinicAdminDTO clinicAdminDTO = ClinicAdminDTO.builder()
               .id(1)
               .username("john_doe")
               .firstName("John")
               .lastName("Doe")
               .email("test@example.com")
               .dateOfBirth(LocalDate.now().minusYears(25))
               .ClinicName("Clinic")
               .build();
       // Act
       ResponseEntity<Object> response = signUpService.adminSignUp(clinicAdminDTO);
       ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
       // Assert
       assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
       assertEquals("Invalid sign up request", responseMessageDto.getMessage());
       assertFalse(responseMessageDto.isSuccess());

   }

    @Test
    public void adminSignUp_withValidClinicAdminDTO_ShouldReturnSuccess() {
        // Arrange
        Address address = Address.builder()
                .city("City")
                .country("Country")
                .street("Street")
                .zipCode("12345")
                .build();
        ClinicAdminDTO clinicAdminDTO = ClinicAdminDTO.builder()
                .username("john_doe")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .email("test@example.com")
                .dateOfBirth(LocalDate.now().minusYears(25))
                .ClinicName("Clinic")
                .address(address)
                .build();
        // Create a mock ClinicAdmin object to simulate repository save result
        ClinicAdmin savedClinicAdmin = ClinicAdmin.builder()
                .username(clinicAdminDTO.getUsername())
                .password("encodedPassword123")
                .firstName(clinicAdminDTO.getFirstName())
                .lastName(clinicAdminDTO.getLastName())
                .email(clinicAdminDTO.getEmail())
                .role(Role.ADMIN)
                .phoneNumber(clinicAdminDTO.getPhoneNumber())
                .age(signUpService.calculateAge(clinicAdminDTO.getDateOfBirth()))
                .birthDate(clinicAdminDTO.getDateOfBirth())
                .build();
        // Mock password encoding
        when(passwordEncoder.encode(clinicAdminDTO.getPassword())).thenReturn("encodedPassword123");
        when(clinicAdminRepository.save(any())).thenReturn(savedClinicAdmin);
        when(jwtService.generateToken(anyMap(), any())).thenReturn("token");

        // Act
        ResponseEntity<Object> response = signUpService.adminSignUp(clinicAdminDTO);
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        String expectedMessage = responseMessageDto.getMessage();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Admin registered successfully, waiting for verification", responseMessageDto.getMessage());
        assertTrue(responseMessageDto.isSuccess());

    }


}
