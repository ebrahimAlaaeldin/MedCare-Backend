package com.example.medcare.entities;
import com.example.medcare.embedded.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Builder
@Data
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clinicId;

    private String name;


    @Embedded
    private Address address;

    private Boolean isVerified;

    private Boolean isActive;

    private LocalDate createdAt;
    @OneToOne
    @JoinColumn(name = "clinicAdminId")
    private ClinicAdmin clinicAdmin;
}
