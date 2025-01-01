package com.example.medcare.service;

import com.example.medcare.Enums.WeekDays;
import com.example.medcare.dto.ClinicDoctorAssignmentDTO;
import com.example.medcare.dto.DoctorClinicDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.DoctorClinic;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorClinicsRepository;
import com.example.medcare.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AssignClinicDoctorServiceTest {

    @Mock
    private DoctorClinicsRepository doctorClinicsRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private AssignClinicDoctorService assignClinicDoctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void assignDoctorToClinic() {
        ClinicDoctorAssignmentDTO dto = ClinicDoctorAssignmentDTO.builder()
                .doctorUsername("doctor123")
                .clinicId(1)
                .weekDay(WeekDays.MONDAY)
                .startTime("09:00")
                .endTime("12:00")
                .build();

        Doctor doctor = new Doctor();
        doctor.setId(1);
        when(doctorRepository.findByUsername(dto.getDoctorUsername())).thenReturn(Optional.of(doctor));
        when(doctorClinicsRepository.hasTimeConflict(eq(dto.getClinicId()), eq(doctor.getId()), eq(dto.getWeekDay()),
                eq(LocalTime.parse(dto.getStartTime())), eq(LocalTime.parse(dto.getEndTime())))).thenReturn(false);

        ResponseMessageDto response = assignClinicDoctorService.assignDoctorToClinic(dto);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Doctor assigned to clinic successfully", response.getMessage());

        verify(doctorClinicsRepository).assignDoctorSchedule(eq(doctor.getId()), eq(dto.getClinicId()), eq(dto.getWeekDay().name()),
                eq(LocalTime.parse(dto.getStartTime())), eq(LocalTime.parse(dto.getEndTime())));
    }

    @Test
    void saveDoctorClinic() {
        ClinicDoctorAssignmentDTO dto = ClinicDoctorAssignmentDTO.builder()
                .doctorId(1)
                .clinicId(1)
                .weekDay(WeekDays.MONDAY)
                .startTime("09:00")
                .endTime("12:00")
                .build();

        assignClinicDoctorService.saveDoctorClinic(dto);

        verify(doctorClinicsRepository).assignDoctorSchedule(eq(dto.getDoctorId()), eq(dto.getClinicId()), eq(dto.getWeekDay().name()),
                eq(LocalTime.parse(dto.getStartTime())), eq(LocalTime.parse(dto.getEndTime())));
    }

    @Test
    void getDoctorClinicAssignment() {
        DoctorClinic doctorClinic = new DoctorClinic();
        Clinic clinic = new Clinic();
        clinic.setClinicId(1);
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setUsername("doctor123");
        doctor.setEmail("doctor123@example.com");

        doctorClinic.setClinicId(clinic);
        doctorClinic.setDoctorId(doctor);
        doctorClinic.setWeekDay(WeekDays.MONDAY);
        doctorClinic.setStartTime(LocalTime.parse("09:00"));
        doctorClinic.setEndTime(LocalTime.parse("12:00"));

        when(doctorClinicsRepository.findAllByClinicId(1)).thenReturn(List.of(doctorClinic));

        ResponseMessageDto response = (ResponseMessageDto) assignClinicDoctorService.getDoctorClinicAssignment(1);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Doctor clinic assignment", response.getMessage());

        List<DoctorClinicDTO> data = (List<DoctorClinicDTO>) response.getData();
        assertEquals(1, data.size());
        assertEquals("doctor123", data.get(0).getDoctorUsername());
    }

    @Test
    void getAssigneByDay() {
        DoctorClinicDTO dto = DoctorClinicDTO.builder()
                .clinicName("Clinic A")
                .weekDay(WeekDays.MONDAY)
                .build();

        Clinic clinic = new Clinic();
        clinic.setClinicId(1);
        clinic.setName("Clinic A");

        DoctorClinic doctorClinic = new DoctorClinic();
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setUsername("doctor123");
        doctor.setEmail("doctor123@example.com");

        doctorClinic.setClinicId(clinic);
        doctorClinic.setDoctorId(doctor);
        doctorClinic.setWeekDay(WeekDays.MONDAY);
        doctorClinic.setStartTime(LocalTime.parse("09:00"));
        doctorClinic.setEndTime(LocalTime.parse("12:00"));

        when(clinicRepository.findByName(dto.getClinicName())).thenReturn(Optional.of(clinic));
        when(doctorClinicsRepository.findAllByClinicIdAndWeekDay(eq(clinic), eq(dto.getWeekDay()))).thenReturn(List.of(doctorClinic));

        ResponseMessageDto response = (ResponseMessageDto) assignClinicDoctorService.getAssigneByDay(dto);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Doctor clinic assignment", response.getMessage());

        List<DoctorClinicDTO> data = (List<DoctorClinicDTO>) response.getData();
        assertEquals(1, data.size());
        assertEquals("doctor123", data.get(0).getDoctorUsername());
    }

    @Test
    void getAssigneByDayNoData() {
        DoctorClinicDTO dto = DoctorClinicDTO.builder()
                .clinicName("Clinic A")
                .weekDay(WeekDays.MONDAY)
                .build();

        Clinic clinic = new Clinic();
        clinic.setClinicId(1);
        clinic.setName("Clinic A");

        when(clinicRepository.findByName(dto.getClinicName())).thenReturn(Optional.of(clinic));
        when(doctorClinicsRepository.findAllByClinicIdAndWeekDay(eq(clinic), eq(dto.getWeekDay()))).thenReturn(Collections.emptyList());

        ResponseMessageDto response = (ResponseMessageDto) assignClinicDoctorService.getAssigneByDay(dto);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Doctor clinic assignment", response.getMessage());

        List<DoctorClinicDTO> data = (List<DoctorClinicDTO>) response.getData();
        assertTrue(data.isEmpty());
    }
}
