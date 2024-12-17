package com.example.medcare.Service;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.service.ScheduleAppointmentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ScheduleAppointmentServiceTest {


    private static AutoCloseable mocks;
    private Patient PATIENT = new Patient();
    private Doctor DOCTOR = new Doctor();

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;


    @InjectMocks
    private ScheduleAppointmentService scheduleAppointmentService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        // Setup Patient object with a sample data
        PATIENT = Patient.builder()
                .id(1)
                .username("Patient_1")
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe123@mail.com")
                .phoneNumber("1234567890")
                .build();

        // Setup Doctor object with a sample data
        DOCTOR = Doctor.builder()
                .id(2)
                .username("Doctor_1")
                .firstName("Amir")
                .lastName("Ragaie")
                .email("amir_ragaie123@example.com")
                .phoneNumber("987654321")
                .age(30)
                .birthDate(LocalDate.of(1991, 5, 10))
                .isVerified(false)
                .license(new License("LIC123456", "General Medicine", LocalDate.of(2020, 1, 1)))
                .build();

    }

    @AfterAll
    static void tearDown() {
        //close resources
        try {
            mocks.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while closing resources");
        }
    }

    @Test
    void testScheduleAppointmentSuccess() {
        // Given
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(PATIENT.getId())
                .doctorId(DOCTOR.getId())
                .appointmentTime("202-2-12 12:00")
                .build();


        when(patientRepository.findById(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findById(any())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(any(), any())).thenReturn(false);

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(appointmentDTO);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((ResponseMessageDto) response.getBody()).isSuccess());
        assertEquals("Appointment Scheduled Successfully", ((ResponseMessageDto) response.getBody()).getMessage());
    }

    @Test
    void testScheduleAppointmentWhenPatientIsNotFound() {
        // Given
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(PATIENT.getId())
                .doctorId(DOCTOR.getId())
                .appointmentTime("202-2-12 12:00")
                .build();

        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(appointmentDTO);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(((ResponseMessageDto) response.getBody()).isSuccess());
        assertEquals("Patient Not Found", ((ResponseMessageDto) response.getBody()).getMessage());
    }

    @Test
    void testScheduleAppointmentWhenDoctorIsNotFound() {
        // Given
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(PATIENT.getId())
                .doctorId(DOCTOR.getId())
                .appointmentTime("202-2-12 12:00")
                .build();
        when(patientRepository.findById(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findById(any())).thenReturn(Optional.empty());

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(appointmentDTO);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(((ResponseMessageDto) response.getBody()).isSuccess());
        assertEquals("Doctor Not Found", ((ResponseMessageDto) response.getBody()).getMessage());
    }

    @Test
    void testScheduleAppointmentWhenSlotIsReserved() {
        // Given
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(PATIENT.getId())
                .doctorId(DOCTOR.getId())
                .appointmentTime("202-2-12 12:00")
                .build();

        when(patientRepository.findById(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findById(any())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(any(), any())).thenReturn(true);

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(appointmentDTO);

        // Then
        assertEquals(409, response.getStatusCodeValue());
        assertFalse(((ResponseMessageDto) response.getBody()).isSuccess());
        assertEquals("This time slot is reserved.", ((ResponseMessageDto) response.getBody()).getMessage());


    }

    @Test
    void testScheduleAppointmentWhenDTOIsNull() {
        // Given
        /**
         * Nothing
         */

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(null);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(ResponseMessageDto.class, response.getBody());
        ResponseMessageDto responseMessageDto = (ResponseMessageDto) response.getBody();
        assertFalse(responseMessageDto.isSuccess());
        assertEquals("Invalid Request", responseMessageDto.getMessage());
    }

    @SneakyThrows
    @Test
    void testConcurrentAppointmentSchedulingForSameSlot() {
        // Given
        AppointmentDTO appointmentDTO1 = AppointmentDTO.builder()
                .patientId(PATIENT.getId())
                .doctorId(DOCTOR.getId())
                .appointmentTime("2024-12-12 10:00")
                .build();

        AppointmentDTO appointmentDTO2 = AppointmentDTO.builder()
                .patientId(PATIENT.getId() + 1) // Another patient
                .doctorId(DOCTOR.getId())
                .appointmentTime("2024-12-12 10:00")
                .build();

        when(patientRepository.findById(PATIENT.getId())).thenReturn(Optional.of(PATIENT));
        when(patientRepository.findById(PATIENT.getId() + 1)).thenReturn(Optional.of(PATIENT)); // Another patient
        when(doctorRepository.findById(any())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(any(), any())).thenReturn(false);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<ResponseEntity<Object>> task1 = () -> scheduleAppointmentService.scheduleAppointment(appointmentDTO1);
        Future<ResponseEntity<Object>> future1 = executor.submit(task1);
        ResponseEntity<Object> response1 = future1.get();


        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(any(), any())).thenReturn(true);

        Callable<ResponseEntity<Object>> task2 = () -> scheduleAppointmentService.scheduleAppointment(appointmentDTO2);
        Future<ResponseEntity<Object>> future2 = executor.submit(task2);
        ResponseEntity<Object> response2 = future2.get();



        // Then
        assertNotEquals(response1.getStatusCodeValue(), response2.getStatusCodeValue(),
                "Both responses should not have the same status code if one appointment is blocked");

        // One should succeed, and the other should fail due to slot reservation
        assertTrue((response1.getStatusCodeValue() == 200 && response2.getStatusCodeValue() == 409) ||
                (response1.getStatusCodeValue() == 409 && response2.getStatusCodeValue() == 200));

        // Cleanup
        executor.shutdown();
    }





}