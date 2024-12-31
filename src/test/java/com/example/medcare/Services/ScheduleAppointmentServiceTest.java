package com.example.medcare.Services;

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
import com.example.medcare.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Mock
    private     UserRepository userRepository;


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
                .patientUsername(PATIENT.getUsername())
                .doctorUsername(DOCTOR.getUsername())
                .appointmentDate("2024-12-12")
                .appointmentTime("10:00")
                .build();


        when(patientRepository.findByUsername(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findByUsername(any())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(any(), any())).thenReturn(false);

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
                        .patientUsername(PATIENT.getUsername())
                        .doctorUsername(DOCTOR.getUsername())
                        .appointmentDate("2024-12-12")
                        .appointmentTime("10:00")
                        .build();


        when(patientRepository.findByUsername(any())).thenReturn(Optional.empty());

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
                .patientUsername(PATIENT.getUsername())
                .doctorUsername(DOCTOR.getUsername())
                .appointmentDate("2024-12-12")
                .appointmentTime("10:00")
                .build();

        when(patientRepository.findByUsername(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findByUsername(any())).thenReturn(Optional.empty());

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
                        .patientUsername(PATIENT.getUsername())
                        .doctorUsername(DOCTOR.getUsername())
                        .appointmentDate("2024-12-12")
                        .appointmentTime("10:00")
                        .build();


        when(patientRepository.findByUsername(any())).thenReturn(Optional.of(PATIENT));
        when(doctorRepository.findByUsername(any())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(any(), any())).thenReturn(true);

        // When
        ResponseEntity<Object> response = scheduleAppointmentService.scheduleAppointment(appointmentDTO);

        // Then
        assertEquals(409, response.getStatusCodeValue());
        assertFalse(((ResponseMessageDto) response.getBody()).isSuccess());
        assertEquals("This time slot is reserved.", ((ResponseMessageDto) response.getBody()).getMessage());


    }

    @Test
    void testScheduleAppointmentWhenDTOIsNull() {

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
                .patientUsername(PATIENT.getUsername())
                .doctorUsername(DOCTOR.getUsername())
                .appointmentDate("2024-12-12")
                .appointmentTime("10:00")
                .build();


        AppointmentDTO appointmentDTO2 = AppointmentDTO.builder()
                .patientUsername(PATIENT.getUsername() + "2")
                .doctorUsername(DOCTOR.getUsername())
                .appointmentDate("2024-12-12")
                .appointmentTime("10:00")
                .build();



        when(patientRepository.findByUsername(PATIENT.getUsername())).thenReturn(Optional.of(PATIENT));
        when(patientRepository.findByUsername(PATIENT.getUsername() + "2")).thenReturn(Optional.of(PATIENT)); // Another patient
        when(doctorRepository.findByUsername(DOCTOR.getUsername())).thenReturn(Optional.of(DOCTOR));
        when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(any(), any())).thenReturn(false);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<ResponseEntity<Object>> task1 = () -> scheduleAppointmentService.scheduleAppointment(appointmentDTO1);
        Future<ResponseEntity<Object>> future1 = executor.submit(task1);
        ResponseEntity<Object> response1 = future1.get();


        when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(any(), any())).thenReturn(true);

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

    @Test
    public void testViewAppointmentsForNullUsername() {
        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient(null);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((ResponseMessageDto) response.getBody()).getMessage().contains("Invalid Request"));
    }

    @Test
    public void testViewAppointmentsForEmptyUsername() {
        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient("   ");
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((ResponseMessageDto) response.getBody()).getMessage().contains("Invalid Request"));
    }
    @Test
    void whenUsernameIsNull_thenBadRequest() {
        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient(null);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid Request: Username is null or empty.", ((ResponseMessageDto) response.getBody()).getMessage());
    }

    @Test
    void whenUsernameIsEmpty_thenBadRequest() {
        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient("   ");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid Request: Username is null or empty.", ((ResponseMessageDto) response.getBody()).getMessage());
    }
    @Test
    void whenUserNotFound_thenRespondNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient("nonexistent");
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Patient Not Found", ((ResponseMessageDto) response.getBody()).getMessage());
    }


    @Test
    void whenNoAppointmentsFound_thenRespondNoAppointments() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new Patient()));
        when(appointmentRepository.findByPatient_Username("user")).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient("user");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("No Appointments Found", ((ResponseMessageDto) response.getBody()).getMessage());
    }
    @Test
    void whenValidAppointmentsExist_thenReturnThem() {
        Patient patient = new Patient();
        patient.setUsername("user");
        Doctor doctor = new Doctor();
        doctor.setUsername("doc");

        Appointment validAppointment = new Appointment();
        validAppointment.setAppointmentId(1);
        validAppointment.setPatient(patient);
        validAppointment.setDoctor(doctor);
        validAppointment.setAppointmentDateTime(LocalDateTime.now());
        validAppointment.setConfirmed(true);
        validAppointment.setCancelled(false);

        Appointment cancelledAppointment = new Appointment();
        cancelledAppointment.setCancelled(true);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatient_Username("user")).thenReturn(Arrays.asList(validAppointment, cancelledAppointment));

        ResponseEntity<Object> response = scheduleAppointmentService.viewAppointmentsForCertainPatient("user");
        assertEquals(200, response.getStatusCodeValue());
        List<AppointmentDTO> results = (List<AppointmentDTO>) ((ResponseMessageDto) response.getBody()).getData();
        assertTrue(results.size() == 1); // Only one valid appointment
        assertFalse(results.get(0).isCancelled());
        assertEquals("Appointments for user", ((ResponseMessageDto) response.getBody()).getMessage());
    }


}