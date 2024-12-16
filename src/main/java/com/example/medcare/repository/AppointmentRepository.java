package com.example.medcare.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.medcare.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    boolean existsByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);
    Appointment findByDoctorIdAndAppointmentTime(Integer doctorId, String appointmentTime);
    Appointment findByAppointmentId(Integer appointmentId);
}