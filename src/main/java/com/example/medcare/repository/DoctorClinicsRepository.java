package com.example.medcare.repository;

import com.example.medcare.Enums.WeekDays;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.DoctorClinic;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;


@Repository
public interface DoctorClinicsRepository extends JpaRepository<DoctorClinic, Integer> {


    @Modifying
    @Query(value = "INSERT INTO doctor_clinic (doctor_id, clinic_id, week_day, start_time, end_time) " +
            "VALUES (:doctorId, :clinicId, :weekDay, :startTime, :endTime)", nativeQuery = true)
    void assignDoctorSchedule(
            @Param("doctorId") int doctorId,
            @Param("clinicId") int clinicId,
            @Param("weekDay") String weekDay,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query("SELECT CASE WHEN COUNT(dc) > 0 THEN TRUE ELSE FALSE END " +
            "FROM DoctorClinic dc " +
            "WHERE dc.clinicId.clinicId = :clinicId " +
            "AND dc.doctorId.id = :doctorId " +
            "AND dc.weekDay = :weekDay " +
            "AND ((:startTime BETWEEN dc.startTime AND dc.endTime) " +
            "OR (:endTime BETWEEN dc.startTime AND dc.endTime) " +
            "OR (dc.startTime BETWEEN :startTime AND :endTime) " +
            "OR (dc.endTime BETWEEN :startTime AND :endTime))")
    boolean hasTimeConflict(
            @Param("clinicId") int clinicId,
            @Param("doctorId") int doctorId,
            @Param("weekDay") WeekDays weekDay,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query("SELECT dc FROM DoctorClinic dc WHERE dc.clinicId.clinicId = :clinicId")
    List<DoctorClinic> findAllByClinicId(
            @Param("clinicId") Integer clinicId
            );

    List<DoctorClinic> findAllByClinicIdAndWeekDay(
            Clinic clinicId,
            WeekDays weekDay
    );
}
