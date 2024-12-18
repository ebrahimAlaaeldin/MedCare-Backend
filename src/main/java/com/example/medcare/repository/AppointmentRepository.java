package com.example.medcare.repository;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Patient;
import com.example.medcare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
//    boolean existsByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);
    boolean existsByDoctorUsernameAndAppointmentDateTime(String doctorUsername, LocalDateTime appointmentDateTime);

//    Appointment findByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);
    Appointment findByAppointmentId(Integer appointmentId);
    List<Appointment> findAllByPatient(Patient patient);

    List<Appointment> findByPatient_Username(String patientUsername);

}

