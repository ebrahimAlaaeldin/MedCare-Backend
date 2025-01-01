package com.example.medcare.service;


import com.example.medcare.Enums.WeekDays;
import com.example.medcare.dto.ClinicDoctorAssignmentDTO;
import com.example.medcare.dto.DoctorClinicDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.DoctorClinic;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorClinicsRepository;
import com.example.medcare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignClinicDoctorService {

    private final DoctorClinicsRepository doctorClinicsRepository;
    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;


    // i will take from the front day and start and end time

    @Transactional
    public ResponseMessageDto assignDoctorToClinic(ClinicDoctorAssignmentDTO clinicDoctorAssignmentDTO) {
        Integer doctorId = doctorRepository.findByUsername(clinicDoctorAssignmentDTO.getDoctorUsername()).get().getId();
        clinicDoctorAssignmentDTO.setDoctorId(doctorId);
        boolean hasTimeConflict = doctorClinicsRepository.hasTimeConflict(
                clinicDoctorAssignmentDTO.getClinicId(),
                clinicDoctorAssignmentDTO.getDoctorId(),
                clinicDoctorAssignmentDTO.getWeekDay(),
                LocalTime.parse(clinicDoctorAssignmentDTO.getStartTime()),
                LocalTime.parse(clinicDoctorAssignmentDTO.getEndTime())
        );

        if (hasTimeConflict) {
            return ResponseMessageDto.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Doctor has time conflict")
                    .data("Doctor has time conflict")
                    .build();
        } else {
            try {
                saveDoctorClinic(clinicDoctorAssignmentDTO);
                return ResponseMessageDto.builder()
                        .statusCode(200)
                        .success(true)
                        .message("Doctor assigned to clinic successfully")
                        .data("Doctor assigned to clinic successfully")
                        .build();
            } catch (Exception e) {
                return ResponseMessageDto.builder()
                        .statusCode(400)
                        .success(false)
                        .message(e.getMessage())
                        .build();
            }
        }


    }


    @Transactional
    public void saveDoctorClinic(ClinicDoctorAssignmentDTO clinicDoctorAssignmentDTO) {
        doctorClinicsRepository.assignDoctorSchedule(
                clinicDoctorAssignmentDTO.getDoctorId(),
                clinicDoctorAssignmentDTO.getClinicId(),
                clinicDoctorAssignmentDTO.getWeekDay().name(),
                LocalTime.parse(clinicDoctorAssignmentDTO.getStartTime()),
                LocalTime.parse(clinicDoctorAssignmentDTO.getEndTime())
        );

    }

    public Object getDoctorClinicAssignment(Integer clinicId) {
        List<DoctorClinic> clinicDoctors = doctorClinicsRepository.findAllByClinicId(clinicId);
        List<DoctorClinicDTO> doctorClinicDTOS = clinicDoctors.stream().map(doctorClinic -> {
            DoctorClinicDTO doctorClinicDTO = DoctorClinicDTO.builder()
                    .doctorUsername(doctorClinic.getDoctorId().getUsername())
                    .doctorEmail(doctorClinic.getDoctorId().getEmail())
                    .doctorId(doctorClinic.getDoctorId().getId())
                    .clinicId(doctorClinic.getClinicId().getClinicId())
                    .weekDay(doctorClinic.getWeekDay())
                    .startTime(doctorClinic.getStartTime())
                    .endTime(doctorClinic.getEndTime())
                    .build();
            return doctorClinicDTO;
        }).toList();


        return ResponseMessageDto.builder()
                .statusCode(200)
                .success(true)
                .message("Doctor clinic assignment")
                .data(doctorClinicDTOS)
                .build();
    }


    // receive clinic name and day only
    public Object getAssigneByDay(DoctorClinicDTO day) {

        Clinic clinic = clinicRepository.findByName(day.getClinicName()).get();
        List<DoctorClinic> doctorClinics = doctorClinicsRepository.findAllByClinicIdAndWeekDay(clinic, day.getWeekDay());
        List<DoctorClinicDTO> doctorClinicDTOS = doctorClinics.stream().map(doctorClinic -> {
            DoctorClinicDTO doctorClinicDTO = DoctorClinicDTO.builder()
                    .doctorUsername(doctorClinic.getDoctorId().getUsername())
                    .doctorEmail(doctorClinic.getDoctorId().getEmail())
                    .doctorId(doctorClinic.getDoctorId().getId())
                    .clinicId(doctorClinic.getClinicId().getClinicId())
                    .weekDay(doctorClinic.getWeekDay())
                    .startTime(doctorClinic.getStartTime())
                    .endTime(doctorClinic.getEndTime())
                    .build();
            return doctorClinicDTO;
        }).toList();

        return ResponseMessageDto.builder()
                .statusCode(200)
                .success(true)
                .message("Doctor clinic assignment")
                .data(doctorClinicDTOS)
                .build();
    }
}
