package com.example.medcare.repository;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    boolean existsByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);

    Appointment findByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);
    Appointment findByAppointmentId(Integer appointmentId);
    List<Appointment> findAllByPatient(Patient patient);


}