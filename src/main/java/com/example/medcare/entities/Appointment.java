package com.example.medcare.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;


    @ManyToOne
    @JoinColumn(name = "patient_username", referencedColumnName = "username", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_username", referencedColumnName = "username", nullable = false)
    private Doctor doctor;
    @Column
    private boolean reminded;

    @Column(nullable = false)
    //format "yyyy-MM-dd HH:mm"
    private LocalDateTime appointmentDateTime;

    private boolean isConfirmed;

    private boolean isCancelled;

    private LocalDateTime createdAt;

    public Appointment(){
        this.createdAt = LocalDateTime.now();
    }

    public String getAppointmentTime() {
        return appointmentDateTime.toString().replace("T", " ");
    }
}
