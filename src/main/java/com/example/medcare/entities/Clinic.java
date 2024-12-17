package com.example.medcare.entities;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clinicId;

    private String name;

    @Embedded
    private ClinicPermit permit;

    @Embedded
    private Address address;

    
    private Boolean isVerified;

    private Boolean isActive;

    private LocalDate createdAt;

    @OneToOne()
    @JoinColumn(name = "clinicAdminId")
    private ClinicAdmin clinicAdmin;
}
